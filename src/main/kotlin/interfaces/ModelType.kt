package me.gsycl2004.interfaces

enum class ModelType(val value:String){
    SAFE("safe-diffusion"),
    FULL("nai-diffusion"),
    FURRY("nai-diffusion-furry"),
    None("none")
}