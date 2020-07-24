/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2020-2020 the original author or authors.
 */

package football.repository;

import football.QuickPerfBeanConfig;
import football.entity.Player;
import football.entity.Team;
import org.junit.jupiter.api.Test;
import org.quickperf.junit5.QuickPerfTest;
import org.quickperf.sql.annotation.ExpectJdbcBatching;
import org.quickperf.sql.annotation.ExpectSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@Import(QuickPerfBeanConfig.class)
@DataJpaTest()
@QuickPerfTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @ExpectSelect(1)
    @Test
    public void should_find_all_players() {
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(2);
    }

    @Test
    @ExpectJdbcBatching(batchSize = 1)
    void should_delete_in_batch() {
        // when
        teamRepository.deleteAll();
    }

    @Test
    @ExpectJdbcBatching(batchSize = 1)
    void should_insert_in_batch() {
        // given
        Team team = teamRepository.findAll()
                .stream()
                .findAny()
                .orElseThrow(() -> new AssertionError("No team exists"));

        Player player = aNewPlayer(team);

        // when
        playerRepository.saveAll(singletonList(player));
    }

    private Player aNewPlayer(Team team) {
        Player player = new Player();
        player.setId((long) 2);
        player.setFirstName("someFirstName");
        player.setLastName("someLastName");
        player.setTeam(team);
        return player;
    }
}
