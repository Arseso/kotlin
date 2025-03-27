package ru.yarsu

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.beust.jcommander.Parameters

@Parameters
class Params {
    @Parameter(names = ["--woutEq"],
        description = "param wout equation")
    var woutEq: Boolean = false
}

@Parameters(separators = "=")
class Args {

    @Parameter(names = ["--req"], description = "Required Argument", required = true)
    var req: String? = null

    @Parameter(names = ["--suck"], description = "who is suck")
    var suck: String? = null


    fun hasSuck(): Boolean {
        if(suck == null)
            return false
        return true
    }
}

fun main(args: Array<String>) {
    val arguments = Args()
    val parameters = Params()
    val jcom = JCommander.newBuilder()
        .addObject(arguments)
        .addObject(parameters)
        .build()

    try {
        jcom.parse(*args)
        println("Parameter without equation used: ${parameters.woutEq}")
        println("Required argument value: ${arguments.req}")
        println(if(arguments.hasSuck()) "Sucking people: ${arguments.suck}" else "")
    } catch (e: ParameterException) {
        println("ERROR: " + e.message)
        jcom.usage()
    }
}
