package com.fandy.news.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Data(

	@field:SerializedName("remember_exp")
	val rememberExp: Any? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("last_login")
	val lastLogin: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("oauth_uid")
	val oauthUid: Any? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("ip_address")
	val ipAddress: String? = null,

	@field:SerializedName("forgot_exp")
	val forgotExp: Any? = null,

	@field:SerializedName("remember_time")
	val rememberTime: Any? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("last_activity")
	val lastActivity: String? = null,

	@field:SerializedName("top_secret")
	val topSecret: Any? = null,

	@field:SerializedName("oauth_provider")
	val oauthProvider: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("banned")
	val banned: String? = null,

	@field:SerializedName("verification_code")
	val verificationCode: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
