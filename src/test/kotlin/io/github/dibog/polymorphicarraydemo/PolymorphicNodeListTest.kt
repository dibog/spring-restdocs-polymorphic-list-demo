package io.github.dibog.polymorphicarraydemo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.dibog.polymorphicarraydemo.JsonPathSubsectionExtractor.Companion.beneathJsonPath
import io.github.dibog.polymorphicarraydemo.entities.Node
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestInstance(PER_CLASS)
class PolymorphicNodeListTest {
    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    private val list = listOf(
        Node.A(1),
        Node.B(true),
        Node.B(false),
        Node.A(2)
    )

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider)
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun documentFetchTree() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andDo(document("fetch-tree",
                responseFields(
                    beneathJsonPath("\$[0]").withSubsectionId("typeA"),
                    fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description("only node types 'A' and 'B' are supported"),
                    fieldWithPath("a")
                        .type(JsonFieldType.NUMBER)
                        .description("specific field for node type A")
                ),
                responseFields(
                    beneathJsonPath("\$[1]").withSubsectionId("typeB"),
                    fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description("only node types 'A' and 'B' are supported"),
                    fieldWithPath("b")
                        .type(JsonFieldType.BOOLEAN)
                        .description("specific field for node type A")
                )))
    }

    @Test
    fun documentStoreTree() {
        val jsonList = ObjectMapper().registerKotlinModule().writeValueAsString(list)

        mockMvc.perform(put("/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonList))
            .andExpect(status().isOk)
            .andDo(document("store-tree",
                requestFields(
                    beneathJsonPath("\$[0]").withSubsectionId("typeA"),
                    fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description("only node types 'A' and 'B' are supported"),
                    fieldWithPath("a")
                        .type(JsonFieldType.NUMBER)
                        .description("specific field for node type A")
                ),
                requestFields(
                    beneathJsonPath("\$[1]").withSubsectionId("typeB"),
                    fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description("only node types 'A' and 'B' are supported"),
                    fieldWithPath("b")
                        .type(JsonFieldType.BOOLEAN)
                        .description("specific field for node type A")
                )
            ))
    }
}
