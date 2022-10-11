package ApiData

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Parameters(
    var height: Int = 768,
    var n_samples: Int = 1,
    var noise: Double = 0.1,
    var sampler: String = "k_euler_ancestral",
    var scale: Int = 13,
    var seed: Int = Random.nextInt(1000000000,2000000000),
    var steps: Int = 28,
    var strength: Double = 0.7,
    var uc: String = "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry",
    var ucPreset: Int = 0,
    var width: Int = 512,
    var image:String? = null,
    var qualityToggle:Boolean = true

)

