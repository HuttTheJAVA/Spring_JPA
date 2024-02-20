package hellojpa;

import jakarta.persistence.*;

import java.util.List;

import static hellojpa.RoleType.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMemeber = em.find(Member.class,member.getId());
            List<Member> members = findMemeber.getTeam().getMembers();

            for(Member m : members){
                System.out.println("m = "+m.getUsername());
            }

            tx.commit();

        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
            emf.close();
        }
    }
}
