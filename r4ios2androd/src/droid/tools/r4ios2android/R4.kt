package droid.tools.r4ios2android

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class R4 {

}

val androidCnStringsFile = "/Users/guo/thingtrust/ta/wallet/src/main/res/values/strings.xml"
val iosCnStringFile = "/Users/guo/thingtrust/ta/mylocale/ioscode/TrustNote/Others/zh-Hans.lproj/Localizable.strings"
val iosEnStringFile = "/Users/guo/thingtrust/ta/mylocale/ioscode/TrustNote/Others/en.lproj/Localizable.strings"


fun main(args: Array<String>) {

    val androidStrings = readAndroidString()

    val iosCnStrings = readIosString(iosCnStringFile)
    val iosEnStrings = readIosString(iosEnStringFile)
    val iosEnStringsKeyToValue = mutableMapOf<String, String>()
    iosEnStrings.forEach { t, u ->
        iosEnStringsKeyToValue.put(u, t)
    }

    val matchedEnResult = mutableMapOf<String, String>()
    androidStrings.forEach { t, u ->
        if (iosCnStrings.containsKey(t)) {
            if (iosEnStringsKeyToValue.containsKey(iosCnStrings[t])) {
                matchedEnResult.put(u, iosEnStringsKeyToValue[iosCnStrings[t]]?:"")
            }
        }
    }

    for(one in matchedEnResult) {
        println("""${one.key}  ->  ${one.value}""")
    }


}

fun readIosString(filePath: String): Map<String, String> {

    val res = mutableMapOf<String, String>()

    val iosPattern = Regex(""""(.*)" *= *"(.*)";""")

    File(filePath).forEachLine {
        val matchRes = iosPattern.matchEntire(it)
        if (matchRes != null) {
            val key = matchRes.groupValues[1]
            val value = matchRes.groupValues[2]
            res.put(value, key)
        }
    }
    return res
}


fun readAndroidString(): Map<String, String> {

    val xlmFile: File = File(androidCnStringsFile)
    val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xlmFile)

    xmlDoc.documentElement.normalize()

    println("Root Node:" + xmlDoc.documentElement.nodeName)

    val stringList: NodeList = xmlDoc.getElementsByTagName("string")

    val res = mutableMapOf<String, String>()
    for (i in 0..stringList.length - 1) {
        var oneString: Node = stringList.item(i)

        if (oneString.getNodeType() === Node.ELEMENT_NODE) {
            res.put(oneString.textContent, oneString.attributes.getNamedItem("name").textContent)
        }
    }
    return res
}