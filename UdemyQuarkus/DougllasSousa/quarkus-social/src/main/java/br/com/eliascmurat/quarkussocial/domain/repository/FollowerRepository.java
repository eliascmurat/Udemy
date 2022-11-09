package br.com.eliascmurat.quarkussocial.domain.repository;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import br.com.eliascmurat.quarkussocial.domain.model.Follower;
import br.com.eliascmurat.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> { 
    public boolean follows(User followerUser, User user) {
        Parameters params = Parameters.with("followerUser", followerUser).and("user", user);
        PanacheQuery<Follower> query = find("followerUser = :followerUser and user = :user", params);
        return query.firstResultOptional().isPresent();
    }

    public List<Follower> findByUser(User user) {
        return find("user", user).list();
    }

    public List<Follower> findByUser(Long userId) {
        return find("user.id", userId).list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> params = Parameters.with("followerId", followerId).and("userId", userId).map();
        delete("follower.id = :followerId and user.id = :userId", params);
    }
}
