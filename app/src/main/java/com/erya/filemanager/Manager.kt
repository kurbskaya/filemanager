package com.erya.filemanager

import android.util.Log
import kotlinx.coroutines.flow.combineTransform
import java.io.File
import java.io.IOException
import java.util.*

class Manager {
    var mShowHiddenFiles = false
    lateinit var mPathStack: Stack<String>
    lateinit var mDirContent: ArrayList<String>
    var sort1 = 1

    fun create() {
        mDirContent = ArrayList<String>()
        mPathStack = Stack<String>()

        mPathStack.push("/")
        mPathStack.push(mPathStack.peek() + "sdcard")
    }

    fun current() : String {
        return mPathStack.peek()
    }

    fun setHomeDir(name: String): ArrayList<String> {
        mPathStack.clear()
        mPathStack.push("/")
        mPathStack.push(name)

        return dirList(1)
    }

    fun dirList(sort: Int): ArrayList<String> {

        if(!mDirContent.isEmpty())
            mDirContent.clear()

        val file = File(mPathStack.peek())

        if(file.exists() && file.canRead()) {
            val list = file.list()
            val len = list?.size
            for (i in 0 until len!!) {
                if(!mShowHiddenFiles) {
                    if(list[i].toString()[0] != '.')
                        mDirContent.add(list[i])

                } else {
                    mDirContent.add(list[i])
                }
            }
        } else {
            mDirContent.add("Emtpy")
        }

        mDirContent = switchSort(sort)

        return mDirContent
    }

    fun getSize2(name: String): Int {
        val size = mPathStack.size
        val temp1 = Stack<String>()
        for (i in mPathStack) {
            temp1.push(i)
        }
        if(size == 1)
            temp1.push("/" + name)
        else
            temp1.push(temp1.peek() + "/" + name)

        val temp2 = ArrayList<String>()

        val file = File(temp1.peek())

        println("temp1")
        println(temp1)

        if(file.exists() && file.canRead() && file.list() != null) {
            val list = file.list()
            val len = list?.size
            for (i in 0 until len!!) {
                if(!mShowHiddenFiles && isDirectory(name)) {
                    if(list[i].toString()[0] != '.')
                        temp2.add(list[i])

                } else if (isDirectory(name)){
                    temp2.add(list[i])
                }
            }
        } else {
        }

        return temp2.size
    }

    fun getNextDirectory(path: String, sort: Int): ArrayList<String> {
        val size = mPathStack.size
        if(size == 1)
            mPathStack.push("/" + path)
        else
            mPathStack.push(mPathStack.peek() + "/" + path)
        sort1 = sort

        return dirList(sort)
    }

    fun isDirectory(name: String): Boolean {
        return File(mPathStack.peek() + "/" + name).isDirectory
    }

    fun getFullName(name: String): String {
        return mPathStack.peek() + "/" + name
    }

    fun getPreviousDir(): ArrayList<String> {
        val size = mPathStack.size

        if (size >= 2)
            mPathStack.pop()

        else if(size == 0)
            mPathStack.push("/")

        return dirList(sort1)
    }

    private val alph = Comparator{ p1: String, p2: String ->
        p1.lowercase(Locale.getDefault()).compareTo(p2.lowercase(Locale.getDefault()))
    }

    private val size = Comparator{arg0: String, arg1: String ->
            val dir = mPathStack.peek()
            val first = File(dir + "/" + arg0).length()
            val second = File(dir + "/" + arg1).length()
            first.compareTo(second)
        }

    fun getSize1(fullName: String): Long {
        return getSize(File(fullName))
    }


    fun getSize(path: File): Long{
        val list = path.listFiles()
        var len = 0
        var s = 0L
        if(list != null) {
            len = list.size
            for (i in 0 until len) {
                try{
                    if(list[i].isFile() && list[i].canRead()) {
                        s += list[i].length()

                    } else if(list[i].isDirectory() && list[i].canRead() && !isSymlink(list[i])) {
                        getSize(list[i])
                    }
                } catch(e: IOException) {
                }
            }
        }
        return s
    }

    private fun isSymlink(file: File?): Boolean {
        var fileInCanonicalDir: File? = null
        if (file?.parent == null) {
            fileInCanonicalDir = file
        } else {
            val canonicalDir = file.getParentFile().getCanonicalFile()
            fileInCanonicalDir = File(canonicalDir, file.getName())
        }
        return !fileInCanonicalDir?.getCanonicalFile()?.equals(fileInCanonicalDir.getAbsoluteFile())!!
    }

    fun switchSort(type: Int): ArrayList<String> {
        if (type == 1) {
            val tt = mDirContent
            println(tt)
            //mDirContent.clear()
            val sorted = tt.sortedBy{it.lowercase(Locale.getDefault())}
            mDirContent.clear()
            for (a in sorted) {
                mDirContent.add(a)
            }
        } else if (type==2) {
            var index = 0
            val size_ar = mDirContent
            val dir = mPathStack.peek()
            val sorted = size_ar.sortedWith(size)

            mDirContent.clear()
            for (a in sorted) {
                if(File(dir + "/" + a).isDirectory())
                    mDirContent.add(index++, a)
                else
                    mDirContent.add(a)
            }
        }
        return mDirContent
    }

}