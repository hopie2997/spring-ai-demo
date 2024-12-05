package com.hopie.springaidemo.support

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/chat")
class ChatController {
    @GetMapping
    fun chat(): String {
        return "chat"
    }
}
