package com.example.chatapplive

class MessageModel {
    var msgId: String? = null
    var senderId: String? = null
    var message: String? = null

    constructor()

    constructor(msgId: String?, senderId: String?, message: String?) {
        this.msgId = msgId
        this.senderId = senderId
        this.message = message
    }
}

