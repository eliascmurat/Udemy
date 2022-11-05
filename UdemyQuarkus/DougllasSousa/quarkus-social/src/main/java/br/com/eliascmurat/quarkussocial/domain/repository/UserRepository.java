package br.com.eliascmurat.quarkussocial.domain.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.eliascmurat.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> { }
