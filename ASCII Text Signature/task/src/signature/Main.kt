package signature

import java.io.File
import java.util.*
import kotlin.math.abs

const val SIDE_DISTANCE = 2
const val BORDER_THICKNESS = 2
const val BORDER_CHAR = "8"

class Letter(val charView: Char, val width: Int) {
    val layers = ArrayList<String>()

    fun setLayers(index: Int, layer: String) {
        layers.add(index, layer)
    }
}

class Alphabet(fileName: String, val wordSeparatorLength: Int) {
    private val letters = ArrayList<Letter>()
    var height = 0
    private var count = 0
    init {
        val file = File(fileName)
        val scanner = Scanner(file)


        val line = scanner.nextLine().split(" ").map { it.toInt() }
        height = line[0]
        count = line[1]

        var index = 0

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isEmpty()) {
                continue
            }
            val letter = Letter(line.split(" ")[0][0], line.split(" ")[1].toInt())
            var layerIndex = 0

            while (layerIndex < height) {
                val layer = scanner.nextLine()
                if (layer.isEmpty()) {
                    continue
                }
                letter.setLayers(layerIndex, layer)
                layerIndex++
            }

            letters.add(letter)
            index++
        }
    }

    fun getLetterByChar(char: Char): Letter? {
        return letters.find { it.charView == char }
    }

    fun calcStringLength(string: String): Int {
        var length = 0
        for (char in string) {
            length += if (char == ' ') {
                wordSeparatorLength
            } else {
                getLetterByChar(char)!!.width
            }
        }
        return length
    }
}

class Word(val word: String, val alphabet: Alphabet) {
    val length = alphabet.calcStringLength(word)
    val height = alphabet.height
    private val layers = ArrayList<String>()

    init {
        for (i in 0 until height) {
            layers.add("")
        }
        for (char in word) {
            if (char == ' ') {
                for (i in 0 until height) {
                    layers[i] += " ".repeat(alphabet.wordSeparatorLength)
                }
            } else {
                val letter = alphabet.getLetterByChar(char)!!
                for (i in 0 until height) {
                    layers[i] += letter.layers[i]
                }
            }
        }
    }

    operator fun get(index: Int): String {
        return layers[index]
    }
}

fun calcBorderLength(nameSurname: Word, status: Word): Int {
    val borderLength: Int
    val nameSurnameLength = nameSurname.alphabet.calcStringLength(nameSurname.word)
    val statusLength = status.alphabet.calcStringLength(status.word)
    borderLength = if (nameSurnameLength > statusLength) {
        nameSurnameLength + SIDE_DISTANCE * 2 + BORDER_THICKNESS * 2
    } else {
        statusLength + SIDE_DISTANCE * 2 + BORDER_THICKNESS * 2
    }

    return borderLength
}

fun main() {
    val medium = Alphabet("medium.txt", 5)
    val roman = Alphabet("roman.txt", 10)

    print("Enter name and surname: ")
    val nameSurname = Word(readln(), roman)

    print("Enter person's status: ")
    val status = Word(readln(), medium)

    val borderLength = calcBorderLength(nameSurname, status)

    // top border
    println(BORDER_CHAR.repeat(borderLength))

    if (nameSurname.length > status.length) {
        for (i in 0 until nameSurname.height) {
            print(BORDER_CHAR.repeat(BORDER_THICKNESS))
            print(" ".repeat(SIDE_DISTANCE))
            print(nameSurname[i])
            print(" ".repeat(SIDE_DISTANCE))
            println(BORDER_CHAR.repeat(BORDER_THICKNESS))
        }

        for (i in 0 until status.height) {
            print(BORDER_CHAR.repeat(BORDER_THICKNESS))
            print(" ".repeat((borderLength - status.length - 4) / 2))
            print(status[i])
            print(" ".repeat((borderLength - status.length - 4) / 2 + abs(borderLength % 2 - status.length % 2)))
            println(BORDER_CHAR.repeat(BORDER_THICKNESS))
        }


    } else {
        for (i in 0 until nameSurname.height) {
            print(BORDER_CHAR.repeat(BORDER_THICKNESS))
            print(" ".repeat((borderLength - nameSurname.length - 4) / 2 ))
            print(nameSurname[i])
            print(" ".repeat((borderLength - nameSurname.length - 4)/ 2 + abs(borderLength % 2 - nameSurname.length % 2)))
            println(BORDER_CHAR.repeat(BORDER_THICKNESS))
        }


        for (i in 0 until status.height) {
            print(BORDER_CHAR.repeat(BORDER_THICKNESS))
            print(" ".repeat(SIDE_DISTANCE))
            print(status[i])
            print(" ".repeat(SIDE_DISTANCE))
            println(BORDER_CHAR.repeat(BORDER_THICKNESS))
        }

    }

    // bottom border
    println(BORDER_CHAR.repeat(borderLength))
}