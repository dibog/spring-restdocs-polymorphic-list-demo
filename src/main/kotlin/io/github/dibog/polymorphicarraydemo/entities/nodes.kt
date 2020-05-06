package io.github.dibog.polymorphicarraydemo.entities

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use=NAME, include=PROPERTY, property="type")
@JsonSubTypes(
    Type(value= Node.A::class, name="A"),
    Type(value= Node.B::class, name="B")
)
sealed class Node(val type: String) {
    data class A(val a: Int): Node("A")
    data class B(val b: Boolean): Node("B")
}
