package hellojpa;

import jakarta.persistence.*;

import static hellojpa.RoleType.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            for(int i =0;i<53;i++){
                Member member = new Member();
                if(i%2==0) {
                    member.setRoleType(USER);
                }else{
                    member.setRoleType(ADMIN);
                }
                em.persist(member);
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
