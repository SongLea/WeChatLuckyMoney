package com.songlea.hongbao.util

import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

/**
 * 红包标记判断
 *
 * @author Song Lea
 */
class LuckyMoneySignature {

    var sender: String = ""
    var content: String = ""
    var time: String = ""
    var contentDescription = ""
    var commentString: String? = null
    var others: Boolean = false

    fun generateSignature(node: AccessibilityNodeInfo, excludeWords: String): Boolean {
        try {
            /* The hongbao container node. It should be a LinearLayout. By specifying that, we can avoid text messages. */
            val hongbaoNode = node.parent
            if ("android.widget.LinearLayout" != hongbaoNode.className) return false
            /* The text in the hongbao. Should mean something. */
            val hongbaoContent = hongbaoNode.getChild(0).text.toString()
            if ("查看红包" == hongbaoContent) return false
            /* Check the user's exclude words list. */
            val excludeWordsArray = excludeWords.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (word in excludeWordsArray)
                if (word.isNotEmpty() && hongbaoContent.contains(word)) return false
            /* The container node for a piece of message. It should be inside the screen.
                Or sometimes it will get opened twice while scrolling. */
            val messageNode = hongbaoNode.parent
            val bounds = Rect()
            messageNode.getBoundsInScreen(bounds)
            if (bounds.top < 0) return false
            /* The sender and possible timestamp. Should mean something too. */
            val hongbaoInfo = getSenderContentDescriptionFromNode(messageNode)
            if (this.getSignature(hongbaoInfo[0], hongbaoContent, hongbaoInfo[1]) == this.toString()) return false
            /* So far we make sure it's a valid new coming hongbao. */
            this.sender = hongbaoInfo[0]
            this.time = hongbaoInfo[1]
            this.content = hongbaoContent
            return true
        } catch (e: Exception) {
            Log.e(LuckyMoneySignature::class.java.name, e.message)
            return false
        }
    }

    override fun toString(): String {
        return this.getSignature(this.sender, this.content, this.time)!!
    }

    private fun getSignature(vararg strings: String): String? {
        var signature = ""
        for (str in strings) {
            signature += str + "|"
        }
        return signature.substring(0, signature.length - 1)
    }

    private fun getSenderContentDescriptionFromNode(node: AccessibilityNodeInfo): Array<String> {
        val count = node.childCount
        val result = arrayOf("unknownSender", "unknownTime")
        (0 until count)
                .map { node.getChild(it) }
                .forEach {
                    if ("android.widget.ImageView" == it.className && "unknownSender" == result[0]) {
                        val contentDescription = it.contentDescription
                        if (contentDescription != null) result[0] = contentDescription.toString()
                                .replace("头像$".toRegex(), "")
                    } else if ("android.widget.TextView" == it.className && "unknownTime" == result[1]) {
                        val thisNodeText = it.text
                        if (thisNodeText != null) result[1] = thisNodeText.toString()
                    }
                }
        return result
    }

    fun cleanSignature() {
        this.content = ""
        this.time = ""
        this.sender = ""
    }
}
