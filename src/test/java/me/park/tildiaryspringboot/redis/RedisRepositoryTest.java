package me.park.tildiaryspringboot.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisRepositoryTest {

	@Autowired
	private RedisRepository redisRepository;

	@AfterEach
	public void tearDown() throws Exception {
		redisRepository.deleteAll();
	}

	@Test
	void 등록_조회기능() {

		// given
		Long userId = 1L;
		RefreshRedisToken insertRefreshToken = RefreshRedisToken.createToken(userId, "token-test");

		// when
		redisRepository.save(insertRefreshToken);

		// then
		RefreshRedisToken selectRefreshToken = redisRepository.findById(userId).orElse(null);
		assertThat(selectRefreshToken).isNotNull();
		assertThat(selectRefreshToken.getToken()).isEqualTo("token-test");
	}

	@Test
	void 수정기능() {

		// given
		Long userId = 1L;
		redisRepository.save(RefreshRedisToken.createToken(userId, "token-test"));

		// when
		RefreshRedisToken selectRefreshToken = redisRepository.findById(userId).orElse(null);
		assertThat(selectRefreshToken).isNotNull();
		assertThat(selectRefreshToken.getToken()).isEqualTo("token-test");
		selectRefreshToken.updateToken("token-test2");
		redisRepository.save(selectRefreshToken);

		// then
		RefreshRedisToken reselectRefreshToken = redisRepository.findById(userId).orElse(null);
		assertThat(reselectRefreshToken).isNotNull();
		assertThat(reselectRefreshToken.getToken()).isEqualTo("token-test2");
	}

}