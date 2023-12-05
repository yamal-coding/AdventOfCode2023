#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 5: https://adventofcode.com/2023/day/5
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

data class Range(
    val start: Long,
    val end: Long,
)

val seedRanges = mutableListOf<Range>()
val rawSeedRanges = scanner.nextLine().split(": ")[1].split(" ").map { it.toLong()}
var i = 0
while (i < rawSeedRanges.size) {
    val start = rawSeedRanges[i++] 
    seedRanges.add(
        Range(
            start = start,
            end = start + rawSeedRanges[i++] - 1,
        )
    )
}

data class Mapping(
    val sourceRangeStart: Long,
    val sourceRangeEnd: Long,
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
            sourceRangeEnd = map[1] + map[2] - 1,
            destinationRangeStart = map[0],
            rangeLength = map[2],
        )
        
        mappings.add(mapping)

        rawMap = if (scanner.hasNext()) scanner.nextLine() else ""
    }
    
    return mappings
}

println("Min location is: ${findMinLocation()}")

fun findMinLocation(): Long {
    var minLocation = Long.MAX_VALUE

    seedRanges.forEach { range ->
        val soils = seedToSoilMappings.mapRange(listOf(range))
        val fertilizers = soilToFertilizerMappings.mapRange(soils)
        val waters = fertilizerToWaterMappings.mapRange(fertilizers)
        val lights = waterToLightMappings.mapRange(waters)
        val temperatures = lightToTemperatureMappings.mapRange(lights)
        val humiditys = temperatureToHumidityMappings.mapRange(temperatures)
        val locations = humidityToLocationMappings.mapRange(humiditys)

        val location = locations.map { it.start }.min()

        if (location < minLocation) {
            minLocation = location
        }
    }
    
    return minLocation
}

fun List<Mapping>.mapRange(ranges: List<Range>): List<Range> {
    val mappedRanges = mutableListOf<Range>()
    var sourceRanges = ranges
    
    forEach { mapping ->
        val remainingRanges = mutableListOf<Range>()
        sourceRanges.forEach { sourceRange ->
            when {
                sourceRange.start >= mapping.sourceRangeStart && sourceRange.end <= mapping.sourceRangeEnd -> { // [ ( ) ]
                    mappedRanges.add(
                        Range(mapping.map(sourceRange.start), mapping.map(sourceRange.end))
                    )
                }
                sourceRange.start <= mapping.sourceRangeStart && sourceRange.end >= mapping.sourceRangeEnd -> { // ( [ ] )
                    mappedRanges.add(
                        Range(mapping.map(mapping.sourceRangeStart), mapping.map(mapping.sourceRangeEnd))
                    )
                    remainingRanges.add(
                        Range(sourceRange.start, mapping.sourceRangeStart - 1)
                    )
                    remainingRanges.add(
                        Range(mapping.sourceRangeEnd + 1, sourceRange.end)
                    )
                }
                sourceRange.start <= mapping.sourceRangeStart && sourceRange.end >= mapping.sourceRangeStart && sourceRange.end <= mapping.sourceRangeEnd -> { // ( [ ) ]
                    mappedRanges.add(
                        Range(mapping.map(mapping.sourceRangeStart), mapping.map(sourceRange.end))
                    )
                    remainingRanges.add(
                        Range(sourceRange.start, mapping.sourceRangeStart - 1)
                    )
                }
                sourceRange.end >= mapping.sourceRangeEnd && sourceRange.start <= mapping.sourceRangeEnd && sourceRange.start >= mapping.sourceRangeStart -> { // [ ( ] )
                    mappedRanges.add(
                        Range(mapping.map(sourceRange.start), mapping.map(mapping.sourceRangeEnd))
                    )
                    remainingRanges.add(
                        Range(mapping.sourceRangeEnd + 1, sourceRange.end)
                    )
                } else -> {
                    remainingRanges.add(
                        Range(sourceRange.start, sourceRange.end)
                    )
                }
                
            }
        }
        sourceRanges = remainingRanges
    }

    mappedRanges.addAll(sourceRanges)

    return mappedRanges
}

fun List<Mapping>.map(source: Long): Long =
    getMapping(source).map(source)

fun List<Mapping>.getMapping(source: Long): Mapping? = 
    firstOrNull { mapping ->
        source >= mapping.sourceRangeStart && source <= mapping.sourceRangeStart + mapping.rangeLength - 1
    }

fun Mapping?.map(source: Long): Long =
    this?.let { mapping ->
        val result = mapping.destinationRangeStart + (source - mapping.sourceRangeStart)

        if (result < 0) {
            println("${mapping.destinationRangeStart} + ($source - ${mapping.sourceRangeStart})")
            println(result)
        }
        result
    } ?: source

