package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemberRepository {
    private static final Map<Long, Member> memberMap = new ConcurrentHashMap<>();
    private static Long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save : member = {}", member);
        memberMap.put(member.getId(), member);
        return member;
    }
    public Member findById(Long id) {
        return memberMap.get(id);
    }
    public List<Member> findAll() {
        return new ArrayList<>(memberMap.values());
    }
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream().
                filter(member -> member.getLoginId().equals(loginId)).
                findAny();
    }
    public void clearStore() {
        memberMap.clear();
    }
}
