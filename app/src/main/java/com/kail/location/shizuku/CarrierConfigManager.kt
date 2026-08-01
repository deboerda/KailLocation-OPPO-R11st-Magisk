package com.kail.location.shizuku

import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import com.kail.location.models.SimCardInfo
import com.kail.location.utils.KailLog
import rikka.shizuku.ShizukuBinderWrapper
import java.lang.reflect.Method

/**
 * 运营商配置管理器 - 非Root模式核心
 * 通过Shizuku调用系统API修改运营商配置（国家码、运营商名称）
 * 
 * 核心技术：
 * 1. ShizukuBinderWrapper - 包装Binder代理
 * 2. TelephonyFrameworkInitializer - 获取系统服务
 * 3. ICarrierConfigLoader - 系统配置加载器接口
 */
object CarrierConfigManager {

    private const val TAG = "CarrierConfigManager"
    
    // 获取ICarrierConfigLoader的Binder包装器
    private fun getCarrierConfigLoaderBinder(): Any? {
        return try {
            val initializer = Class.forName("android.telephony.TelephonyFrameworkInitializer")
            val telephonyServiceManager = initializer.getMethod("getTelephonyServiceManager").invoke(null)
            val registerer = telephonyServiceManager.javaClass
                .getMethod("getCarrierConfigServiceRegisterer")
                .invoke(telephonyServiceManager)
            val carrierConfigService = registerer.javaClass.getMethod("get").invoke(registerer) as? android.os.IBinder
            
            if (carrierConfigService == null) {
                KailLog.w(null, TAG, "carrierConfigService is null")
                return null
            }
            
            // 创建Shizuku Binder包装器
            ShizukuBinderWrapper(carrierConfigService)
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to get carrier config loader: ${e.message}")
            null
        }
    }

    /**
     * 获取设备上所有的SIM卡信息
     */
    fun getSimCards(context: Context): List<SimCardInfo> {
        val simCards = mutableListOf<SimCardInfo>()
        
        try {
            // 获取前两个卡槽的订阅ID
            val subId1 = subscriptionIdsForSlot(0)
            val subId2 = subscriptionIdsForSlot(1)

            if (subId1 != null && subId1.isNotEmpty()) {
                val config1 = getCurrentConfig(subId1[0])
                simCards.add(
                    SimCardInfo(
                        slot = 1,
                        subId = subId1[0],
                        carrierName = getCarrierNameBySubId(context, subId1[0]),
                        currentConfig = config1
                    )
                )
            }

            if (subId2 != null && subId2.isNotEmpty()) {
                val config2 = getCurrentConfig(subId2[0])
                simCards.add(
                    SimCardInfo(
                        slot = 2,
                        subId = subId2[0],
                        carrierName = getCarrierNameBySubId(context, subId2[0]),
                        currentConfig = config2
                    )
                )
            }
            
            KailLog.i(null, TAG, "Found ${simCards.size} SIM cards")
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to get SIM cards: ${e.message}")
        }

        return simCards
    }

    private fun subscriptionIdsForSlot(slot: Int): IntArray? = runCatching {
        SubscriptionManager::class.java
            .getMethod("getSubId", Int::class.javaPrimitiveType)
            .invoke(null, slot) as? IntArray
    }.getOrNull()

    /**
     * 获取指定订阅ID的当前配置
     */
    private fun getCurrentConfig(subId: Int): Map<String, String> {
        return try {
            val binder = getCarrierConfigLoaderBinder() ?: return emptyMap()
            
            // 获取ICarrierConfigLoader接口
            val iCarrierConfigLoaderClass = Class.forName("com.android.internal.telephony.ICarrierConfigLoader")
            val asInterfaceMethod: Method = iCarrierConfigLoaderClass.getMethod("asInterface", android.os.IBinder::class.java)
            val carrierConfigLoader = asInterfaceMethod.invoke(null, binder)
            
            // 调用getConfigForSubId方法
            val getConfigMethod: Method = iCarrierConfigLoaderClass.getMethod("getConfigForSubId", Int::class.javaPrimitiveType, String::class.java)
            val config = getConfigMethod.invoke(carrierConfigLoader, subId, "com.kail.location") as? PersistableBundle
            
            if (config == null) {
                return emptyMap()
            }

            val result = mutableMapOf<String, String>()

            // 获取国家码配置
            config.getString(CarrierConfigManager.KEY_SIM_COUNTRY_ISO_OVERRIDE_STRING)?.let {
                result["国家码"] = it
            }

            // 获取运营商名称配置
            if (config.getBoolean(CarrierConfigManager.KEY_CARRIER_NAME_OVERRIDE_BOOL, false)) {
                config.getString(CarrierConfigManager.KEY_CARRIER_NAME_STRING)?.let {
                    result["运营商名称"] = it
                }
            }

            KailLog.d(null, TAG, "Config for subId $subId: $result")
            result
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to get current config: ${e.message}")
            emptyMap()
        }
    }

    /**
     * 根据订阅ID获取运营商名称
     */
    private fun getCarrierNameBySubId(context: Context, subId: Int): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            ?: return ""

        return try {
            telephonyManager.createForSubscriptionId(subId).networkOperatorName
        } catch (e: Exception) {
            // 如果获取失败，回退到默认的 TelephonyManager
            telephonyManager.networkOperatorName
        }
    }

    /**
     * 设置运营商配置
     * @param subId 订阅ID
     * @param countryCode 国家码（如"CN"、"US"等，2字符）
     * @param carrierName 运营商名称（可选）
     */
    fun setCarrierConfig(subId: Int, countryCode: String?, carrierName: String? = null) {
        try {
            val bundle = PersistableBundle()

            // 设置国家码
            if (!countryCode.isNullOrEmpty() && countryCode.length == 2) {
                bundle.putString(
                    CarrierConfigManager.KEY_SIM_COUNTRY_ISO_OVERRIDE_STRING,
                    countryCode.lowercase()
                )
                KailLog.i(null, TAG, "Setting country code: $countryCode")
            }

            // 设置运营商名称
            if (!carrierName.isNullOrEmpty()) {
                bundle.putBoolean(CarrierConfigManager.KEY_CARRIER_NAME_OVERRIDE_BOOL, true)
                bundle.putString(CarrierConfigManager.KEY_CARRIER_NAME_STRING, carrierName)
                KailLog.i(null, TAG, "Setting carrier name: $carrierName")
            }

            overrideCarrierConfig(subId, bundle)
            KailLog.i(null, TAG, "Successfully set carrier config for subId $subId")
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to set carrier config: ${e.message}")
        }
    }

    /**
     * 重置运营商配置
     * @param subId 订阅ID
     */
    fun resetCarrierConfig(subId: Int) {
        try {
            overrideCarrierConfig(subId, null)
            KailLog.i(null, TAG, "Reset carrier config for subId $subId")
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to reset carrier config: ${e.message}")
        }
    }

    /**
     * 覆盖运营商配置（核心方法）
     */
    private fun overrideCarrierConfig(subId: Int, bundle: PersistableBundle?) {
        try {
            val binder = getCarrierConfigLoaderBinder() ?: throw Exception("Failed to get carrier config loader binder")
            
            // 获取ICarrierConfigLoader接口
            val iCarrierConfigLoaderClass = Class.forName("com.android.internal.telephony.ICarrierConfigLoader")
            val asInterfaceMethod: Method = iCarrierConfigLoaderClass.getMethod("asInterface", android.os.IBinder::class.java)
            val carrierConfigLoader = asInterfaceMethod.invoke(null, binder)
            
            // 调用overrideConfig方法
            val overrideConfigMethod: Method = iCarrierConfigLoaderClass.getMethod(
                "overrideConfig", 
                Int::class.javaPrimitiveType, 
                PersistableBundle::class.java,
                Boolean::class.javaPrimitiveType
            )
            overrideConfigMethod.invoke(carrierConfigLoader, subId, bundle, true)
            
            KailLog.d(null, TAG, "Override config invoked for subId $subId")
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to override carrier config: ${e.message}")
            throw e
        }
    }
}
