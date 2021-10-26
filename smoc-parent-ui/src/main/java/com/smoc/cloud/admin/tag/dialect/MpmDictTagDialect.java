package com.smoc.cloud.admin.tag.dialect;

import com.smoc.cloud.admin.tag.processor.MpmDictTagProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.HashSet;
import java.util.Set;

/**
 * mpm自定义字典标签方言
 * 2019/5/23 15:14
 **/
public class MpmDictTagDialect extends AbstractProcessorDialect implements IProcessorDialect {

    //定义方言名称
    private static final String DIALECT_NAME = "Mpm Dict Dialect";
    private static final String PREFIX = "mpm";

    public MpmDictTagDialect() {
        super(DIALECT_NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MpmDictTagProcessor(dialectPrefix));
        return processors;
    }

}
