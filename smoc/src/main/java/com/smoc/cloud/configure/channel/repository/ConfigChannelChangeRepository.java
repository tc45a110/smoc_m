package com.smoc.cloud.configure.channel.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigChannelChangeRepository extends JpaRepository<ConfigChannelChange, String> {


    PageList<ConfigChannelChangeValidator> page(PageParams<ConfigChannelChangeValidator> pageParams);

    /**
     * 通道切换(变更通道在业务账户中的优先级)
     *
     * @param qo
     */
    void addChannelChange(ConfigChannelChangeValidator qo);

    /**
     * 通道切换修改(变更通道在业务账户中的优先级)
     *
     * @param qo
     * @param originalAccountIds 修改前（原来）操作的业务账号
     */
     void editChannelChange(ConfigChannelChangeValidator qo,String originalAccountIds);

    /**
     * 取消通道变更
     *
     * @param qo
     * @param originalAccountIds
     */
    void cancelChannelChange(ConfigChannelChange qo, String originalAccountIds);
}