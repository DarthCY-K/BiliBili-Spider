package org.bilibili.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.bilibili.service.SearchService;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VideoMapper {
	public static String databaseName = "bilibili_spider";
	public static String datatableName = "video_data";

	@Insert("insert into " + datatableName + " (bTitle, bType, bAuthor, bLike, bCoin, bCollect, bShare, bViews, bBarrages, bUpTime, bLink) values(#{bTitle}, #{bType}, #{bAuthor}, #{bLike}, #{bCoin}, #{bCollect}, #{bShare}, #{bViews}, #{bBarrages}, #{bUpTime}, #{bLink})")
	public int InsertViewVideos(HashMap hashMap);

	@Select("select count(*) from " + datatableName)
	public int QueryViewVideosNumber();

	@Select("select * from " + datatableName + " limit  #{start}, #{limit}")
	public List<Map<String,Object>> QueryViewVideosItem(@Param("start") int start, @Param("limit") int limit);

	@Select("select * from " + datatableName + " where bTitle = #{bTitle} AND bAuthor = #{bAuthor} AND bUpTime = #{bUpTime}")
	public List<HashMap<String,Object>> QueryViewVideosItemWithMainKey(@Param("bTitle")String bTitle, @Param("bAuthor")String bAuthor, @Param("bUpTime")String bUpTime);

	@SelectProvider(type=SearchService.class, method="createQueryViewVideosIntegrateInformationWithBUpTimeSQL")
	public List<HashMap<String,Object>> QueryViewVideosIntegrateInformationWithBUpTime(@Param("yearData")String yearData);

	@SelectProvider(type=SearchService.class, method="createQueryViewVideosIntegrateInformationWithBTypeSQL")
	public List<HashMap<String,Object>> QueryViewVideosIntegrateInformationWithBType(@Param("typeData")String typeData);

	@Select("select distinct bType from " + datatableName)
	public List<HashMap<String,Object>> QueryViewVideosBTypeInformationRemoveDuplication();

	/**
	 * 根据记录在数据库内id，查询视频bv号.
	 *
	 * @param bId       视频记录在数据库内id
	 * @return 视频bv号
	 */
	@SelectProvider(type=SearchService.class,method="createBidVideoSql")
	public String searchVideoBid(@Param("bId") String bId);


	/**
	 * 多条件查询总条数.
	 *
	 * @param bTitle       视频标题
	 * @param bType        视频类型
	 * @param bAuthor      视频作者
	 * @param bLikeMin     最小点赞数
	 * @param bLikeMax     最大点赞数
	 * @param bCoinMin     最小硬币数
	 * @param bCoinMax     最大硬币数
	 * @param bCollectMin  最小收藏数
	 * @param bCollectMax  最大收藏数
	 * @param bShareMin    最小转发数
	 * @param bShareMax    最大转发数
	 * @param bViewsMin    最小观看数
	 * @param bViewsMax    最大观看数
	 * @param bBarragesMin 最小弹幕数
	 * @param bBarragesMax 最大弹幕数
	 * @param bUpTime      视频上传时间
	 * @return the int
	 */
	@SelectProvider(type=SearchService.class,method="createCountVideoSql")
	public int queryVideoTotal(
			@Param("bTitle") String bTitle, @Param("bType") String bType, @Param("bAuthor") String bAuthor,
			@Param("bLikeMin") String bLikeMin, @Param("bLikeMax") String bLikeMax,
			@Param("bCoinMin") String bCoinMin, @Param("bCoinMax") String bCoinMax,
			@Param("bCollectMin") String bCollectMin, @Param("bCollectMax") String bCollectMax,
			@Param("bShareMin") String bShareMin, @Param("bShareMax") String bShareMax,
			@Param("bViewsMin") String bViewsMin, @Param("bViewsMax") String bViewsMax,
			@Param("bBarragesMin") String bBarragesMin, @Param("bBarragesMax") String bBarragesMax,
			@Param("bUpTime") String bUpTime);

	/**
	 * 多条件分页查询数据
	 *
	 * @param start        the start
	 * @param limit        the limit
	 * @param bTitle       视频标题
	 * @param bType        视频类型
	 * @param bAuthor      视频作者
	 * @param bLikeMin     最小点赞数
	 * @param bLikeMax     最大点赞数
	 * @param bCoinMin     最小硬币数
	 * @param bCoinMax     最大硬币数
	 * @param bCollectMin  最小收藏数
	 * @param bCollectMax  最大收藏数
	 * @param bShareMin    最小转发数
	 * @param bShareMax    最大转发数
	 * @param bViewsMin    最小观看数
	 * @param bViewsMax    最大观看数
	 * @param bBarragesMin 最小弹幕数
	 * @param bBarragesMax 最大弹幕数
	 * @param bUpTime      视频上传时间
	 * @return list
	 */
	@SelectProvider(type = SearchService.class,method = "createSearchVideoSql")
	public List<Map<String, Object>> searchVideo(
			@Param("start") int start,@Param("limit") int limit,
			@Param("bTitle") String bTitle, @Param("bType") String bType, @Param("bAuthor") String bAuthor,
			@Param("bLikeMin") String bLikeMin, @Param("bLikeMax") String bLikeMax,
			@Param("bCoinMin") String bCoinMin, @Param("bCoinMax") String bCoinMax,
			@Param("bCollectMin") String bCollectMin, @Param("bCollectMax") String bCollectMax,
			@Param("bShareMin") String bShareMin, @Param("bShareMax") String bShareMax,
			@Param("bViewsMin") String bViewsMin, @Param("bViewsMax") String bViewsMax,
			@Param("bBarragesMin") String bBarragesMin, @Param("bBarragesMax") String bBarragesMax,
			@Param("bUpTime") String bUpTime);
}
