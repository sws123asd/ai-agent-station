package fun.wswj.ai.domain.agent.service.armory;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author sws
 * @Date 2025/7/24 09:46
 * @description: 规则树抽象方法定义 （抽象模板）
 */
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> {

    protected final ApplicationContext applicationContext;
    protected final ThreadPoolExecutor threadPoolExecutor;
    protected final IAgentRepository agentRepository;

    protected AbstractArmorySupport(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository agentRepository) {
        this.applicationContext = applicationContext;
        this.threadPoolExecutor = threadPoolExecutor;
        this.agentRepository = agentRepository;
    }

    @Override
    protected void multiThread(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext){}


    protected String beanName(String id){
        return "default";
    }
    protected <T> T getBean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 自定义beantioDefinition
     * 通过Builder创建GenericBeanDefinition,无需单独注册实例（通过Supplier关联传递进来我们自定义的实例进行创建）
     * 我们再次通过`getBean`获取该bean时，Spring会重新创建。延迟初始化（首次getBean()时调用Supplier）
     * @param beanName 名称
     * @param beanClass 类
     * @param beanInstance 实例
     * @param <T> 泛型
     */
    protected synchronized <T> void registerBean(String beanName, Class<T> beanClass, T beanInstance){
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        // 构建新的beanDefinition
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass, () -> beanInstance)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .setPrimary(true);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        if (beanFactory.containsSingleton(beanName)) {
            beanFactory.destroySingleton(beanName);
        }
        // 若项目初始化启动时自动装配BeanDefinition，移除，使用自定义新的
        if (beanFactory.containsBeanDefinition(beanName)) {
            beanFactory.removeBeanDefinition(beanName);
        }
        // 注册beanDefinition
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

}
