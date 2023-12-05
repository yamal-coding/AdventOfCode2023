#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 5: https://adventofcode.com/2023/day/5
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val seeds = scanner.nextLine().split(": ")[1].split(" ").map { it.toLong()}

data class Mapping(
    val sourceRangeStart: Long,
    val destinationRangeStart: Long,
    val rangeLength: Long, 
)

// empty line
scanner.nextLine()

// seed-to-soil map:
scanner.nextLine()
val seedToSoilMappings = loadMappings()

// soil-to-fertilizer map:
scanner.nextLine()
val soilToFertilizerMappings = loadMappings()

// fertilizer-to-water map:
scanner.nextLine()
val fertilizerToWaterMappings = loadMappings()

// water-to-ligght map:
scanner.nextLine()
val waterToLightMappings = loadMappings()

// light-to-temperature map:
scanner.nextLine()
val lightToTemperatureMappings = loadMappings()

// temperature-to-humidity map:
scanner.nextLine()
val temperatureToHumidityMappings = loadMappings()

// humidity-to-location map:
scanner.nextLine()
val humidityToLocationMappings = loadMappings()


fun loadMappings(): List<Mapping> {
    val mappings = mutableListOf<Mapping>()
    
    var rawMap = scanner.nextLine()
    while (rawMap != "") {
        val map = rawMap.split(" ").map { it.toLong() }
        val mapping = Mapping(
            sourceRangeStart = map[1],
            destinationRangeStart = map[0],
            rangeLength = map[2], 
        )
        
        mappings.add(mapping)

        rawMap = if (scanner.hasNext()) scanner.nextLine() else ""
    }
    
    return mappings
}

val locations = seeds.map { seed ->
    val soil = seedToSoilMappings.map(seed)
    val fertilizer = soilToFertilizerMappings.map(soil)
    val water = fertilizerToWaterMappings.map(fertilizer)
    val light = waterToLightMappings.map(water)
    val temperature = lightToTemperatureMappings.map(light)
    val humidity = temperatureToHumidityMappings.map(temperature)
    val location = humidityToLocationMappings.map(humidity)

    location
}

fun List<Mapping>.map(source: Long): Long =
    firstOrNull { mapping ->
        source >= mapping.sourceRangeStart && source <= mapping.sourceRangeStart + mapping.rangeLength - 1
    }?.let { mapping ->
        mapping.destinationRangeStart + (source - mapping.sourceRangeStart)
    } ?: source


val minLocation = locations.min()

println("Min location is: $minLocation")