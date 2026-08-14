package com.zhangti.talk2computer2

import org.json.JSONObject

/**
 * 本地配置/密钥统一入口。
 *
 * 来源：项目根目录 secrets.properties（已 gitignore）的全部 key=value 条目，
 * 构建时注入 BuildConfig.LOCAL_CONFIG_JSON。不止 API key，baseUrl、model 等配置都可以放。
 *
 * 未来做「用户自己输入」时：
 * 1. 用 DataStore 保存用户输入的配置；
 * 2. 在 [get] 里先查用户配置、查不到再回落本地值。
 *    优先级：用户输入 > 本地构建值。
 */
object LocalConfig {

    private val values: Map<String, String> by lazy {
        val json = BuildConfig.LOCAL_CONFIG_JSON
        if (json.isBlank()) {
            emptyMap()
        } else {
            runCatching {
                val obj = JSONObject(json)
                buildMap {
                    obj.keys().forEach { put(it, obj.optString(it, "")) }
                }
            }.getOrDefault(emptyMap())
        }
    }

    /** 按名字取配置，如 LocalConfig["deepseek"]；没有则返回空串 */
    operator fun get(name: String): String = values[name].orEmpty()
}
