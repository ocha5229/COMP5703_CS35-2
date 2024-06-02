package com.cs35.system.mapper;

import java.util.List;
import com.cs35.system.domain.SysUserPost;

/**
 *
 * @author cs35
 */
public interface SysUserPostMapper
{
    public int deleteUserPostByUserId(Long userId);

    public int countUserPostById(Long postId);

    public int deleteUserPost(Long[] ids);

    public int batchUserPost(List<SysUserPost> userPostList);
}
