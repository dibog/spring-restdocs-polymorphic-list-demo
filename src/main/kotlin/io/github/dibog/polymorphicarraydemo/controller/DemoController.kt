package io.github.dibog.polymorphicarraydemo.controller

import io.github.dibog.polymorphicarraydemo.entities.Node
import io.github.dibog.polymorphicarraydemo.entities.Node.A
import io.github.dibog.polymorphicarraydemo.entities.Node.B
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController() {

    @PutMapping("/", consumes=[MediaType.APPLICATION_JSON_VALUE])
    fun storeList(@RequestBody list: List<Node>) {
        println(list)
    }

    @GetMapping("/", produces=[MediaType.APPLICATION_JSON_VALUE])
    fun fetchList(): List<Node> {
        return listOf(
            A(1),
            B(true),
            B(false),
            A(2)
        )
    }
}