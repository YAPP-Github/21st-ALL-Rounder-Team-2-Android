package com.yapp.gallery.data.room

import androidx.room.*

@Dao
interface TempPostDao {
    // 임시 포스트 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTempPost(post: TempPost)

    // 가장 최근 포스트 받기
    @Query("SELECT * FROM TempPostTable ORDER BY postId DESC LIMIT 1")
    fun getPost(): TempPost

    // 임시 포스트 업데이트
    @Update
    fun updateTempPost(post: TempPost)

    // 임시 포스트 전부 삭제
    @Query("DELETE FROM TempPostTAble")
    fun deletePost()
}