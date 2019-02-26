package cn.libery.tinder

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        var result = ArrayList<ArrayList<Int>>(10)

        val nums = ArrayList<Int>(25)
        nums.add(1000)
        nums.add(500)
        nums.add(500)
        nums.add(100)
        nums.add(100)
        nums.add(100)
        nums.add(50)
        nums.add(50)
        nums.add(50)
        nums.add(50)

        result = combinationSum(intArrayOf(1000, 500, 500, 100, 100, 100, 50, 50, 50), 1000, result)

        val lastResult = TreeMap<Int, ArrayList<Int>>()
        for (arrays in result) {
            var temp = 0
            for (i in arrays) {
                temp += i
            }
            println(temp.toString() + " " + Arrays.toString(arrays.toTypedArray()))
            lastResult[temp] = arrays
        }
        println(result.size)
        println(Arrays.toString(lastResult.lastEntry().value.toTypedArray()))
    }

    private fun combinationSum(num: IntArray?, target: Int, result: ArrayList<ArrayList<Int>>): ArrayList<ArrayList<Int>> {
        val res = ArrayList<ArrayList<Int>>(10)
        if (num == null || num.isEmpty()) return ArrayList(1)
        Arrays.sort(num)
        return dfs(num, 0, target, ArrayList(10), res, result)
    }

    private fun dfs(candidates: IntArray,
                    startIndex: Int,
                    target: Int,
                    combination: ArrayList<Int>,
                    res: ArrayList<ArrayList<Int>>, result: ArrayList<ArrayList<Int>>): ArrayList<ArrayList<Int>> {

        result.add(ArrayList(combination))

        if (target == 0) {
            res.add(ArrayList(combination))
            return result
        }

        for (i in startIndex until candidates.size) {
            if (i != startIndex && candidates[i] == candidates[i - 1]) {
                continue
            }
            if (target < candidates[i]) {
                break
            }
            combination.add(candidates[i])
            dfs(candidates, i + 1, target - candidates[i], combination, res, result)
            combination.removeAt(combination.size - 1)
        }
        return result
    }

    @Test
    fun lruTest() {
        val lru = LruCache<Int, String>()
        for (i in 0..99) {
            lru[i] = "i$i"
        }
        println(lru.size)

        for (entry in lru.entries) {
            println("${entry.key} ${entry.value}")
        }
    }

}
