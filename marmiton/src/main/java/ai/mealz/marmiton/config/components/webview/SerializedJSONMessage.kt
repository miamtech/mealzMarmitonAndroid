package ai.mealz.marmiton.config.components.webview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Message {
    abstract val message: String
}

@Serializable
@SerialName("searchChange")
data class SearchChangeMessage(
    override val message: String,
    val searchTerm: String,
    val numberOfResults: Int
) : Message()

@Serializable
@SerialName("filterChange")
data class FilterChangeMessage(
    override val message: String,
    val supplierName: String
) : Message()

@Serializable
@SerialName("mapSelected")
data class MapSelectedMessage(
    override val message: String
) : Message()

@Serializable
@SerialName("listSelected")
data class ListSelectedMessage(
    override val message: String
) : Message()

@Serializable
@SerialName("showChange")
data class ShowChangeEvent(
    override val message: String,
    val value: Boolean
) : Message()

@Serializable
@SerialName("posIdChange")
data class PosIdChangeEvent(
    override val message: String,
    val posId: String?,
    val posExtId: String?,
    val supplierId: String?,
    val posName: String?,
    val supplierName: String?
) : Message()