package io.github.dibog.polymorphicarraydemo

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.springframework.http.MediaType
import org.springframework.restdocs.payload.PayloadSubsectionExtractor


class JsonPathSubsectionExtractor(
    private val jsonPath: String,
    private val subsectionId: String
) : PayloadSubsectionExtractor<JsonPathSubsectionExtractor> {
    companion object {
        @JvmStatic
        fun beneathJsonPath(path: String, id: String): PayloadSubsectionExtractor<*> {
             return JsonPathSubsectionExtractor(path, id)
        }

        @JvmStatic
        fun beneathJsonPath(path: String): PayloadSubsectionExtractor<*> {
            return JsonPathSubsectionExtractor(path, "beneath-$path")
        }
    }

    override fun getSubsectionId() = subsectionId

    override fun withSubsectionId(subsectionId: String)
        = JsonPathSubsectionExtractor(jsonPath, subsectionId)

    override fun extractSubsection(payload: ByteArray, mediaType: MediaType): ByteArray {
        val elem = JsonPath.parse(String(payload, Charsets.UTF_8)).read<Any>(jsonPath)
        return ObjectMapper().writeValueAsString(elem).toByteArray(Charsets.UTF_8)
    }
}