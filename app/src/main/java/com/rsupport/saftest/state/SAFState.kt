package com.rsupport.saftest.state

sealed class SAFState{
    object Idle: SAFState()
    object Loading: SAFState()
    object OnSAFFile: SAFState()
    object OnSAFFolder: SAFState()
}
