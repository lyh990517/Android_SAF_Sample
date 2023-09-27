package com.rsupport.saftest.model

enum class Attributes(val value: Int) {
    CanCopy(0x01),
    CanDelete(0x02),
    CanRename(0x04),
    CanCreate(0x08)
}