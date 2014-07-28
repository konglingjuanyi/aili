package org.hbhk.maikkr.user.server.service;

import org.hbhk.aili.orm.server.surpport.Page;
import org.hbhk.aili.orm.share.model.Pagination;
import org.hbhk.maikkr.user.share.pojo.BlogInfo;

public interface IBlogService {

	BlogInfo save(BlogInfo blog);

	Pagination<BlogInfo> getBlogPage(BlogInfo blog, Page page);

}