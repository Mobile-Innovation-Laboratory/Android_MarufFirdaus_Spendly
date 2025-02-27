package dev.maruffirdaus.spendly.data.remote.model

import com.google.gson.annotations.SerializedName

data class ItadResponse(

	@field:SerializedName("hasMore")
	val hasMore: Boolean? = null,

	@field:SerializedName("nextOffset")
	val nextOffset: Int? = null,

	@field:SerializedName("list")
	val list: List<ListItem?>? = null
)

data class HistoryLow1y(

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("amountInt")
	val amountInt: Int? = null
)

data class HistoryLow3m(

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("amountInt")
	val amountInt: Int? = null
)

data class Shop(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Regular(

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("amountInt")
	val amountInt: Int? = null
)

data class StoreLow(

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("amountInt")
	val amountInt: Int? = null
)

data class HistoryLow(

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("amountInt")
	val amountInt: Int? = null
)

data class PlatformsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Price(

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("amountInt")
	val amountInt: Int? = null
)

data class ListItem(

	@field:SerializedName("deal")
	val deal: Deal? = null,

	@field:SerializedName("assets")
	val assets: Assets? = null,

	@field:SerializedName("mature")
	val mature: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: Any? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)

data class Assets(

	@field:SerializedName("banner600")
	val banner600: String? = null,

	@field:SerializedName("banner400")
	val banner400: String? = null,

	@field:SerializedName("banner145")
	val banner145: String? = null,

	@field:SerializedName("banner300")
	val banner300: String? = null,

	@field:SerializedName("boxart")
	val boxart: String? = null
)

data class Deal(

	@field:SerializedName("shop")
	val shop: Shop? = null,

	@field:SerializedName("cut")
	val cut: Int? = null,

	@field:SerializedName("flag")
	val flag: Any? = null,

	@field:SerializedName("historyLow_1y")
	val historyLow1y: HistoryLow1y? = null,

	@field:SerializedName("voucher")
	val voucher: Any? = null,

	@field:SerializedName("storeLow")
	val storeLow: StoreLow? = null,

	@field:SerializedName("historyLow")
	val historyLow: HistoryLow? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("platforms")
	val platforms: List<PlatformsItem?>? = null,

	@field:SerializedName("price")
	val price: Price? = null,

	@field:SerializedName("historyLow_3m")
	val historyLow3m: HistoryLow3m? = null,

	@field:SerializedName("expiry")
	val expiry: Any? = null,

	@field:SerializedName("regular")
	val regular: Regular? = null,

	@field:SerializedName("drm")
	val drm: List<DrmItem?>? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class DrmItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
