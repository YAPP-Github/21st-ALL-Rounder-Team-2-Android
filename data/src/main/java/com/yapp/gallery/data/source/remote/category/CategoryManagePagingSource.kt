package com.yapp.gallery.data.source.remote.category

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.di.DispatcherModule
import com.yapp.gallery.domain.entity.category.PostContent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CategoryManagePagingSource @AssistedInject constructor(
    private val artieSerivce: ArtieSerivce,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
    @Assisted private val categoryId: Long
) : PagingSource<Int, PostContent>() {

    @AssistedFactory
    interface CategoryManagePagingFactory {
        fun create(categoryId: Long): CategoryManagePagingSource
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostContent> {
        return try {
            val page = if (params.key != null) params.key!! * PAGE_SIZE else 0
            val response = withContext(dispatcher){
                artieSerivce.getCategoryPost(categoryId, page, PAGE_SIZE)
            }

            val next = params.key ?: 0

            LoadResult.Page(
                data = response.content,
                prevKey = if (next == 0) null else next - 1,
                // 페이징이 마지막이거나 빈 리스트라면 next key null
                nextKey = if (response.empty || response.last) null else next + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, PostContent>): Int? {
        return state.anchorPosition?.let {anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object{
        const val PAGE_SIZE = 20
    }

}