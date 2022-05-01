package com.dodo.fplbot.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "fplEventClient", url = "\${fpl.api.fixtures-url}")
interface FplEventClient {

    @RequestMapping(method = [RequestMethod.GET], params = ["event"])
    fun getContent(@RequestParam("event") event: Int): List<EventDto>?

    @RequestMapping(method = [RequestMethod.GET], params = ["future"])
    fun getFutureContent(@RequestParam("future") event: Int): List<EventDto>?
}