package com.huotu.ymr.repository;

        import com.huotu.ymr.entity.ScoreFlow;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
        import org.springframework.stereotype.Repository;

/**
 * Created by admin on 2016/1/8.
 */

@Repository
public interface ChargeRepository extends JpaRepository<ScoreFlow,Long>, JpaSpecificationExecutor<ScoreFlow>
{

}
