package august.lesincs.simplefilmreview.retrofit

import august.lesincs.simplefilmreview.bean.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Administrator on 2017/9/3.
 */

const val DOUBAN_BASE_URL: String = "http://api.douban.com/v2/movie/"
const val DOUBAN_API_KEY = "0b2bdeda43b5688921839c8ecb20399b"
const val DOUBAN_CLIENT = "923"
const val DOUBAN_UDID = "459"

val DoubanRetrofit: Retrofit
    get() {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(DOUBAN_BASE_URL)
                .build()
    }


interface Top250Service {
    @GET("top250")
    fun getTop250Movies(@Query("start") start: Int,
                        @Query("apiKey") apiKey: String = DOUBAN_API_KEY,
                        @Query("count") count: Int = 15): Observable<MoviePrevBean>

}

interface ComingSoonService {
    @GET("coming_soon")
    fun getComingSoonMovies(@Query("start") start: Int,
                            @Query("apiKey") apiKey: String = DOUBAN_API_KEY,
                            @Query("count") count: Int = 15): Observable<MoviePrevBean>
}

interface InTheatersService {
    @GET("in_theaters")
    fun getInTheaterMovies(@Query("start") start: Int,
                           @Query("apiKey") apiKey: String = DOUBAN_API_KEY,
                           @Query("count") count: Int = 15): Observable<MoviePrevBean>
}


interface MovieDetailService {
    @GET("subject/{id}")
    fun getMovieDetail(@Path("id") id: String,@Query("apiKey") apiKey: String = DOUBAN_API_KEY): Observable<MovieDetailBean>
}

interface CastInfoService{

    @GET("celebrity/{id}")
    fun getCastInfo(@Path("id") id: String,@Query("apiKey") apiKey: String = DOUBAN_API_KEY): Observable<CastInfoBean>

}



interface MovieSearchService {
    @GET("search")
    fun getSearchMovie(@Query("q") q: String,
                       @Query("start") start: Int,
                       @Query("apiKey") apiKey: String = DOUBAN_API_KEY): Observable<MoviePrevBean>
}

interface MovieTagSearchService {
    @GET("search")
    fun getTagMovie(@Query("tag") tag: String,
                    @Query("start") start: Int,
                    @Query("apiKey") apiKey: String = DOUBAN_API_KEY): Observable<MoviePrevBean>

}

interface LongCommentService {

    @GET("subject/{id}/reviews")
    fun getLongComments(@Path("id") id: String,
                        @Query("start") start: Int,
                        @Query("apiKey") apiKey: String = DOUBAN_API_KEY,
                        @Query("client") client: String = DOUBAN_CLIENT,
                        @Query("udid") udid: String = DOUBAN_UDID,
                        @Query("count") count: Int = 10): Observable<LongCommentsBean>
}

interface ShortsCommentService {

    @GET("subject/{id}/comments")
    fun getShortComments(@Path("id") id: String,
                         @Query("start") start: Int,
                         @Query("apiKey") apiKey: String = DOUBAN_API_KEY,
                         @Query("client") client: String = DOUBAN_CLIENT,
                         @Query("udid") udid: String = DOUBAN_UDID,
                         @Query("count") count: Int = 20): Observable<ShortCommentsBean>
}