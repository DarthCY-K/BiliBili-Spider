package org.bilibili.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.bilibili.mappers.VideoMapper;


/**
 * 检索页面的数据库查询服务.
 * @author DarthCY
 * @date 2021/7/5
 */
public class SearchService {
	public static String databaseName = "bilibili_spider";
	public static String datatableName = "video_data";

	/**
	 * 生成通过视频数据库id查询bv号的sql语句
	 *
	 * @param bId 视频数据库id
	 * @return bv号
	 */
	public String createBidVideoSql(@Param("bId") final String bId) {
		return new SQL() {
			{
				SELECT("bLink");
				FROM(datatableName);
				if(bId != null && !"".equals(bId)) { WHERE(" bId = " + bId); }
			}
		}.toString();
	}

	/**
	 * 动态生成，多条件查询数据的sql语句.
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
	 * @return 生成的sql语句
	 */
	public String createCountVideoSql(
			@Param("bTitle") final String bTitle, @Param("bType") final String bType, @Param("bAuthor") final String bAuthor,
			@Param("bLikeMin") final String bLikeMin, @Param("bLikeMax") final String bLikeMax,
			@Param("bCoinMin") final String bCoinMin, @Param("bCoinMax") final String bCoinMax,
			@Param("bCollectMin") final String bCollectMin, @Param("bCollectMax") final String bCollectMax,
			@Param("bShareMin") final String bShareMin, @Param("bShareMax") final String bShareMax,
			@Param("bViewsMin") final String bViewsMin, @Param("bViewsMax") final String bViewsMax,
			@Param("bBarragesMin") final String bBarragesMin, @Param("bBarragesMax") final String bBarragesMax,
			@Param("bUpTime") final String bUpTime) {
		return new SQL() {
			{
				//拼接sql
				SELECT("COUNT(*)");
				FROM(datatableName);

				if(bTitle != null && !"".equals(bTitle)) { WHERE(" bTitle like '%" + bTitle + "%' "); }
				if(bType != null && !"".equals(bType)) { WHERE(" bType like '%" + bType + "%' "); }
				if(bAuthor != null && !"".equals(bAuthor)) { WHERE(" bAuthor like '%" + bAuthor + "%' "); }

				if((bLikeMin != null) && (!"".equals(bLikeMin)) && (Integer.parseInt(bLikeMin)>=0)) { WHERE(" bLike >= " + Integer.parseInt(bLikeMin)); }
				if((bLikeMax != null) && (!"".equals(bLikeMax)) && (Integer.parseInt(bLikeMax)>=0)) { WHERE(" bLike <= " + Integer.parseInt(bLikeMax)); }

				if((bCoinMin != null) && (!"".equals(bCoinMin)) && (Integer.parseInt(bCoinMin)>=0)) { WHERE(" bCoin >= " + Integer.parseInt(bCoinMin)); }
				if((bCoinMax != null) && (!"".equals(bCoinMax)) && (Integer.parseInt(bCoinMax)>=0)) { WHERE(" bCoin <= " + Integer.parseInt(bCoinMax)); }

				if((bCollectMin != null) && (!"".equals(bCollectMin)) && (Integer.parseInt(bCollectMin)>=0)) { WHERE(" bCollect >= " + Integer.parseInt(bCollectMin)); }
				if((bCollectMax != null) && (!"".equals(bCollectMax)) && (Integer.parseInt(bCollectMax)>=0)) { WHERE(" bCollect <= " + Integer.parseInt(bCollectMax)); }

				if((bShareMin != null) && (!"".equals(bShareMin)) && (Integer.parseInt(bShareMin)>=0)) { WHERE(" bShare >= " + Integer.parseInt(bShareMin)); }
				if((bShareMax != null) && (!"".equals(bShareMax)) && (Integer.parseInt(bShareMax)>=0)) { WHERE(" bShare <= " + Integer.parseInt(bShareMax)); }

				if((bViewsMin != null) && (!"".equals(bViewsMin)) && (Integer.parseInt(bViewsMin)>=0)) { WHERE(" bViews >= " + Integer.parseInt(bViewsMin)); }
				if((bViewsMax != null) && (!"".equals(bViewsMax)) && (Integer.parseInt(bViewsMax)>=0)) { WHERE(" bViews <= " + Integer.parseInt(bViewsMax)); }

				if((bBarragesMin != null) && (!"".equals(bBarragesMin)) && (Integer.parseInt(bBarragesMin)>=0)) { WHERE(" bBarrages >= " + Integer.parseInt(bBarragesMin)); }
				if((bBarragesMax != null) && (!"".equals(bBarragesMax)) && (Integer.parseInt(bBarragesMax)>=0)) { WHERE(" bBarrages <= " + Integer.parseInt(bBarragesMax)); }

				if(bUpTime != null && !"".equals(bUpTime)) { WHERE(" bUpTime = " + bUpTime);}
			}
		}.toString();
	}

	/**
	 * 动态生成，多条件分页查询数据的sql语句.
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
	 * @return the string
	 */
	public String createSearchVideoSql(
			@Param("start") final int start,@Param("limit") final int limit,
			@Param("bTitle") final String bTitle, @Param("bType") final String bType, @Param("bAuthor") final String bAuthor,
			@Param("bLikeMin") final String bLikeMin, @Param("bLikeMax") final String bLikeMax,
			@Param("bCoinMin") final String bCoinMin, @Param("bCoinMax") final String bCoinMax,
			@Param("bCollectMin") final String bCollectMin, @Param("bCollectMax") final String bCollectMax,
			@Param("bShareMin") final String bShareMin, @Param("bShareMax") final String bShareMax,
			@Param("bViewsMin") final String bViewsMin, @Param("bViewsMax") final String bViewsMax,
			@Param("bBarragesMin") final String bBarragesMin, @Param("bBarragesMax") final String bBarragesMax,
			@Param("bUpTime") final String bUpTime) {
		return new SQL() {
			{//拼接sql
				SELECT("*");
				FROM(datatableName);
				if(bTitle != null && !"".equals(bTitle)) { WHERE(" bTitle like '%" + bTitle + "%' "); }
				if(bType != null && !"".equals(bType)) { WHERE(" bType like '%" + bType + "%' "); }
				if(bAuthor != null && !"".equals(bAuthor)) { WHERE(" bAuthor like '%" + bAuthor + "%' "); }

				if((bLikeMin != null) && (!"".equals(bLikeMin)) && (Integer.parseInt(bLikeMin)>=0)) { WHERE(" bLike >= " + Integer.parseInt(bLikeMin)); }
				if((bLikeMax != null) && (!"".equals(bLikeMax)) && (Integer.parseInt(bLikeMax)>=0)) { WHERE(" bLike <= " + Integer.parseInt(bLikeMax)); }

				if((bCoinMin != null) && (!"".equals(bCoinMin)) && (Integer.parseInt(bCoinMin)>=0)) { WHERE(" bCoin >= " + Integer.parseInt(bCoinMin)); }
				if((bCoinMax != null) && (!"".equals(bCoinMax)) && (Integer.parseInt(bCoinMax)>=0)) { WHERE(" bCoin <= " + Integer.parseInt(bCoinMax)); }

				if((bCollectMin != null) && (!"".equals(bCollectMin)) && (Integer.parseInt(bCollectMin)>=0)) { WHERE(" bCollect >= " + Integer.parseInt(bCollectMin)); }
				if((bCollectMax != null) && (!"".equals(bCollectMax)) && (Integer.parseInt(bCollectMax)>=0)) { WHERE(" bCollect <= " + Integer.parseInt(bCollectMax)); }

				if((bShareMin != null) && (!"".equals(bShareMin)) && (Integer.parseInt(bShareMin)>=0)) { WHERE(" bShare >= " + Integer.parseInt(bShareMin)); }
				if((bShareMax != null) && (!"".equals(bShareMax)) && (Integer.parseInt(bShareMax)>=0)) { WHERE(" bShare <= " + Integer.parseInt(bShareMax)); }

				if((bViewsMin != null) && (!"".equals(bViewsMin)) && (Integer.parseInt(bViewsMin)>=0)) { WHERE(" bViews >= " + Integer.parseInt(bViewsMin)); }
				if((bViewsMax != null) && (!"".equals(bViewsMax)) && (Integer.parseInt(bViewsMax)>=0)) { WHERE(" bViews <= " + Integer.parseInt(bViewsMax)); }

				if((bBarragesMin != null) && (!"".equals(bBarragesMin)) && (Integer.parseInt(bBarragesMin)>=0)) { WHERE(" bBarrages >= " + Integer.parseInt(bBarragesMin)); }
				if((bBarragesMax != null) && (!"".equals(bBarragesMax)) && (Integer.parseInt(bBarragesMax)>=0)) { WHERE(" bBarrages <= " + Integer.parseInt(bBarragesMax)); }

				if(bUpTime != null && !"".equals(bUpTime)) { WHERE(" bUpTime = " + bUpTime);}
				LIMIT("#{start},#{limit}");
			}
		}.toString();
	}

	public String createQueryViewVideosIntegrateInformationWithBUpTimeSQL(@Param("yearData")String yearData) {
		return new SQL() {
			{
				//拼接sql
				SELECT("*");
				FROM(VideoMapper.datatableName);

				WHERE(" bUpTime like '"+yearData+"%'");
			}
		}.toString();
	}

	public String createQueryViewVideosIntegrateInformationWithBTypeSQL(@Param("typeData")String typeData) {
		return new SQL() {
			{
				//拼接sql
				SELECT("*");
				FROM(VideoMapper.datatableName);

				WHERE(" bType = '" + typeData + "'");
			}
		}.toString();
	}
}
