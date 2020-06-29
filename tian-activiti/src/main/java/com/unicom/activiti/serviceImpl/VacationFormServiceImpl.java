package com.unicom.activiti.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.activiti.service.IVacationFormService;
import com.unicom.generator.entity.VacationForm;
import com.unicom.generator.mapper.VacationFormMapper;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ctf
 * @since 2020-06-17
 */
@Service
public class VacationFormServiceImpl extends ServiceImpl<VacationFormMapper, VacationForm>
    implements IVacationFormService {}
