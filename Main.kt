package parking

enum class ParkingLotStatus (val message: String) {
    PARKING_LOT_NOT_CREATED("Sorry, a parking lot has not been created."),
    PARKING_LOT_EMPTY("Parking lot is empty."),
    PARKING_LOT_FULL("Sorry, the parking lot is full.")
}

data class Car (val registrationNumber: String, val color: String)

data class ParkingLot (var parkingLotSize: Int? = null) {
    val availableSpots = parkingLotSize?.let { List(it) { it + 1 }.toMutableList() }
    var engagedSpots: MutableList<Int?> = mutableListOf()
    var nextAvailableSpot = availableSpots?.minOrNull()
    var parkedCars: MutableMap<Int?, Car> = mutableMapOf()
}

var parkingLot = ParkingLot()

fun create(parkingData: List<String>) {
    parkingLot = ParkingLot(parkingData[1].toInt())
    println("Created a parking lot with ${parkingData[1]} spots.")
}

fun printStatus() {
    if (parkingLot.parkedCars.isNotEmpty()) {
        for (i in parkingLot.parkedCars.keys) {
            println("${i} ${parkingLot.parkedCars[i]?.registrationNumber} ${parkingLot.parkedCars[i]?.color}")
        }
    } else {
        println(ParkingLotStatus.PARKING_LOT_EMPTY.message)
    }
}

fun park (parkingData: List<String>) {
    val car = Car(parkingData[1], parkingData[2])
    val parkingSpot: Int? = parkingLot.availableSpots?.minOrNull()
    if (parkingLot.availableSpots?.isNotEmpty() == true) {
        parkingLot.engagedSpots.add(parkingSpot)
        parkingLot.availableSpots?.remove(parkingSpot)
        parkingLot.nextAvailableSpot = parkingLot.availableSpots?.minOrNull()
        parkingLot.parkedCars[parkingSpot] = car
        println("${car.color} car parked in spot $parkingSpot.")
    } else {
        println(ParkingLotStatus.PARKING_LOT_FULL.message)
    }
}

fun leave(parkingData: List<String>) {
    val spotToLeave = parkingData[1].toInt()
    if (parkingData[1].toInt() in parkingLot.engagedSpots) {
        parkingLot.engagedSpots.remove(spotToLeave)
        parkingLot.availableSpots?.add(spotToLeave)
        parkingLot.nextAvailableSpot = parkingLot.availableSpots?.minOrNull()
        parkingLot.parkedCars.remove(spotToLeave)
        println("Spot $spotToLeave is free.")
    } else {
        println("There is no car in spot $spotToLeave.")
    }
}

fun printRegsByColor(parkingData: List<String>) {
    val colorToCheck = parkingData[1]
    val regsByColor = mutableListOf<String>()

    for (i in parkingLot.parkedCars.values) {
        if (colorToCheck.lowercase() == i.color.lowercase()) {
            regsByColor.add(i.registrationNumber)
        }
    }
    if (regsByColor.isNotEmpty()) {
        println(regsByColor.joinToString(", "))
    } else {
        println("No cars with color $colorToCheck were found.")
    }
}

fun printSpotsByColor(parkingData: List<String>) {
    val colorToCheck = parkingData[1]
    val spotsByColor = mutableListOf<Int?>()

    for (i in parkingLot.parkedCars) {
        if (colorToCheck.lowercase() == i.value.color.lowercase()) {
            spotsByColor.add(i.key)
        }
    }
    if (spotsByColor.isNotEmpty()) {
        println(spotsByColor.joinToString(", "))
    } else {
        println("No cars with color $colorToCheck were found.")
    }
}

fun printSpotByReg(parkingData: List<String>) {
    val regToCheck = parkingData[1]
    var spotByReg: Int? = null

    for (i in parkingLot.parkedCars) {
        if (regToCheck.lowercase() == i.value.registrationNumber.lowercase()) {
            spotByReg = i.key
        }
    }
    if (spotByReg != null) {
        println(spotByReg)
    } else {
        println("No cars with registration number $regToCheck were found.")
    }
}

fun main() {
    while (true) {
        val parkingData = readln().split(" ")
        if (parkingData[0] != "exit") {
            if (parkingData[0] != "create") {
                if (parkingLot.parkingLotSize == null) {
                    println(ParkingLotStatus.PARKING_LOT_NOT_CREATED.message)
                } else {
                    when (parkingData[0]) {
                        "status" -> printStatus()
                        "reg_by_color" -> printRegsByColor(parkingData)
                        "spot_by_color" -> printSpotsByColor(parkingData)
                        "spot_by_reg" -> printSpotByReg(parkingData)
                        "park" -> park(parkingData)
                        "leave" -> leave(parkingData)
                        else -> println("Wrong command")
                    }
                }
            } else {
                create(parkingData)
            }
        } else {
            break
        }
    }
}
