package com.tinfive.nearbyplace.networks

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        val newReq = chain.request().newBuilder()
            .header("Accept", "application/json")
            //.addHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyIiwianRpIjoiNDdlOTgyZmEwYTk3NDNlODgwNDYyYWZkZDdhOGM1NTU2ZDkxNzM3ZWIyZGM4NWNjMTg2MWE0MDg0M2VkZWUzNjhlNzRiOTYyNDk3MzMzYjYiLCJpYXQiOjE1NzkxNzEyNTgsIm5iZiI6MTU3OTE3MTI1OCwiZXhwIjoxNjEwNzkzNjU4LCJzdWIiOiI3OCIsInNjb3BlcyI6W119.e9hxgHXc784Pnkq5uPAPvZdHWL5rq2s-qLCi5bGk4HZt5PkrjzdoEYa21uQei3F5xaj0zgLO5qMTBJel46R_rcCAV-m1NvKgkT-02x3VxVUimbDGeko7Fc_vu_s1WWHt9qaCYsGnUNj0BuY0FO8FNLJgSIkQCM_c47hx0pyWEZ-aedavg0aJ6VrGc4X6-eMZ0pEc47cI82fq2mshiVdX28V2paVpegCsst1bVKRh17UvdHQCg9Xv0vcN1QDl4eBH517RPiJwWmVlVqZIJ3TDf_oSRf8m-oqz9D60PW_NAeXTT_SKRUppJtKF3HXf31f7t1NgAffGg07O754isW2DV6FbmgMgKctJ8mQL7q-fpmFCHSsFeCS858NVX0D3QA0oFMpTBmI8qXF_fm7AESQuk5H9ESPPwElqhWwRNATPRDyuPxFbB1JJRmTx4U9N_M1I5lBYla4LrHvwCUJZZApSEPyJq6F_Jikn_mstjhi9CRd-fP3mN2pkyyhrwELCtl6PwjLkWL5PX7WP_L4026wu4XZGZm79nDtf8b2W-jpGtCtvw38ZHztq6nu4KGJxgh_I3NM58MyTdjW_0GHYBsTH8fcZVt0BLwcc2Evp-l0lMlmSpptHoIeTzgJtsmUeFpHWXn8YIKUCotIIZQnmXbDvozoEubHig6Isw7nA6uWln1w")
            .build()
        return chain.proceed(newReq)

    }

}