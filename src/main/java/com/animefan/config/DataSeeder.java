package com.animefan.config;

import com.animefan.model.Anime;
import com.animefan.model.Character;
import com.animefan.repository.AnimeRepository;
import com.animefan.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final AnimeRepository animeRepository;
    private final CharacterRepository characterRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            if (animeRepository.count() == 0) {
                // Create 20 Anime objects
                List<Anime> animes = Arrays.asList(
                        Anime.builder().name("Naruto").description("A young ninja strives to become the Hokage.").tags("Action, Ninja, Shounen").status("Finished").rating("8.3").genres("Action, Adventure").studios("Pierrot").origin("Japan").build(),
                        Anime.builder().name("One Piece").description("A boy seeks the legendary One Piece treasure.").tags("Pirate, Adventure, Fantasy").status("Ongoing").rating("9.1").genres("Action, Adventure, Fantasy").studios("Toei Animation").origin("Japan").build(),
                        Anime.builder().name("Attack on Titan").description("Humanity fights against giant Titans.").tags("Titans, Drama, Action").status("Finished").rating("9.0").genres("Action, Drama, Fantasy").studios("Wit Studio").origin("Japan").build(),
                        Anime.builder().name("Death Note").description("A student discovers a deadly notebook.").tags("Psychological, Thriller").status("Finished").rating("8.7").genres("Thriller, Supernatural").studios("Madhouse").origin("Japan").build(),
                        Anime.builder().name("Fullmetal Alchemist: Brotherhood").description("Brothers seek the Philosopher's Stone.").tags("Alchemy, Brothers, Adventure").status("Finished").rating("9.2").genres("Action, Adventure, Fantasy").studios("Bones").origin("Japan").build(),
                        Anime.builder().name("My Hero Academia").description("Superpowers are the norm in this world.").tags("Superhero, School, Action").status("Ongoing").rating("8.0").genres("Action, Superhero").studios("Bones").origin("Japan").build(),
                        Anime.builder().name("Demon Slayer").description("A boy fights demons to save his sister.").tags("Demons, Shounen, Action").status("Ongoing").rating("8.6").genres("Action, Supernatural").studios("ufotable").origin("Japan").build(),
                        Anime.builder().name("Jujutsu Kaisen").description("A student battles curses with sorcery.").tags("Curses, Shounen, Action").status("Ongoing").rating("8.8").genres("Action, Supernatural").studios("MAPPA").origin("Japan").build(),
                        Anime.builder().name("Dragon Ball Z").description("Saiyan warriors defend Earth.").tags("Saiyan, Action, Adventure").status("Finished").rating("8.2").genres("Action, Adventure").studios("Toei Animation").origin("Japan").build(),
                        Anime.builder().name("Bleach").description("A teen becomes a Soul Reaper.").tags("Soul, Action, Supernatural").status("Finished").rating("7.9").genres("Action, Supernatural").studios("Pierrot").origin("Japan").build(),
                        Anime.builder().name("Sword Art Online").description("Players trapped in a virtual world.").tags("Virtual, Game, Adventure").status("Ongoing").rating("7.7").genres("Action, Adventure, Sci-Fi").studios("A-1 Pictures").origin("Japan").build(),
                        Anime.builder().name("Tokyo Ghoul").description("A college student becomes a ghoul.").tags("Ghoul, Horror, Supernatural").status("Finished").rating("7.8").genres("Horror, Supernatural").studios("Pierrot").origin("Japan").build(),
                        Anime.builder().name("Haikyuu!!").description("A boy aims to be a top volleyball player.").tags("Volleyball, Sports, Drama").status("Finished").rating("8.7").genres("Sports, Drama").studios("Production I.G").origin("Japan").build(),
                        Anime.builder().name("Hunter x Hunter").description("A boy becomes a Hunter to find his father.").tags("Hunter, Adventure, Action").status("Finished").rating("9.0").genres("Action, Adventure").studios("Madhouse").origin("Japan").build(),
                        Anime.builder().name("One Punch Man").description("A hero defeats any foe with one punch.").tags("Hero, Comedy, Action").status("Ongoing").rating("8.5").genres("Action, Comedy").studios("Madhouse").origin("Japan").build(),
                        Anime.builder().name("Mob Psycho 100").description("A psychic boy struggles with his powers.").tags("Psychic, Comedy, Action").status("Finished").rating("8.6").genres("Action, Comedy").studios("Bones").origin("Japan").build(),
                        Anime.builder().name("Black Clover").description("A magicless boy aims to be Wizard King.").tags("Magic, Shounen, Action").status("Ongoing").rating("7.8").genres("Action, Fantasy").studios("Pierrot").origin("Japan").build(),
                        Anime.builder().name("Fairy Tail").description("A wizard guild takes on dangerous jobs.").tags("Guild, Magic, Adventure").status("Finished").rating("7.9").genres("Action, Adventure, Fantasy").studios("A-1 Pictures").origin("Japan").build(),
                        Anime.builder().name("Steins;Gate").description("Time travel experiments go awry.").tags("Time, Thriller, Sci-Fi").status("Finished").rating("9.1").genres("Sci-Fi, Thriller").studios("White Fox").origin("Japan").build(),
                        Anime.builder().name("Re:Zero").description("A boy is reborn every time he dies.").tags("Isekai, Drama, Fantasy").status("Ongoing").rating("8.2").genres("Fantasy, Drama").studios("White Fox").origin("Japan").build()
                );
                animeRepository.saveAll(animes);

                // Save characters, each referencing the correct anime
                List<Character> characters = Arrays.asList(
                        Character.builder().name("Naruto Uzumaki").description("Energetic ninja of the Hidden Leaf.").age(17).height("180cm").birthdate("Oct 10").anime(animes.get(0)).build(),
                        Character.builder().name("Sasuke Uchiha").description("Naruto's rival and friend.").age(17).height("182cm").birthdate("Jul 23").anime(animes.get(0)).build(),
                        Character.builder().name("Monkey D. Luffy").description("Captain of the Straw Hat Pirates.").age(19).height("174cm").birthdate("May 5").anime(animes.get(1)).build(),
                        Character.builder().name("Roronoa Zoro").description("Swordsman of the Straw Hat Pirates.").age(21).height("181cm").birthdate("Nov 11").anime(animes.get(1)).build(),
                        Character.builder().name("Eren Yeager").description("Titan shifter and protagonist.").age(15).height("170cm").birthdate("Mar 30").anime(animes.get(2)).build(),
                        Character.builder().name("Mikasa Ackerman").description("Eren's adoptive sister.").age(15).height("170cm").birthdate("Feb 10").anime(animes.get(2)).build(),
                        Character.builder().name("Light Yagami").description("Genius student with a deadly notebook.").age(17).height("179cm").birthdate("Feb 28").anime(animes.get(3)).build(),
                        Character.builder().name("L").description("World-famous detective.").age(24).height("178cm").birthdate("Oct 31").anime(animes.get(3)).build(),
                        Character.builder().name("Edward Elric").description("Young alchemist prodigy.").age(15).height("149cm").birthdate("Feb 3").anime(animes.get(4)).build(),
                        Character.builder().name("Alphonse Elric").description("Edward's younger brother.").age(14).height("220cm").birthdate("Apr 19").anime(animes.get(4)).build(),
                        Character.builder().name("Izuku Midoriya").description("Quirkless boy turned hero.").age(16).height("166cm").birthdate("Jul 15").anime(animes.get(5)).build(),
                        Character.builder().name("Katsuki Bakugo").description("Midoriya's explosive rival.").age(16).height("172cm").birthdate("Apr 20").anime(animes.get(5)).build(),
                        Character.builder().name("Tanjiro Kamado").description("Determined demon slayer.").age(15).height("165cm").birthdate("Jul 14").anime(animes.get(6)).build(),
                        Character.builder().name("Nezuko Kamado").description("Tanjiro's demon sister.").age(14).height("153cm").birthdate("Dec 28").anime(animes.get(6)).build(),
                        Character.builder().name("Yuji Itadori").description("High schooler with a cursed finger.").age(15).height("173cm").birthdate("Mar 20").anime(animes.get(7)).build(),
                        Character.builder().name("Megumi Fushiguro").description("Stoic sorcerer.").age(15).height("175cm").birthdate("Dec 22").anime(animes.get(7)).build(),
                        Character.builder().name("Goku").description("Saiyan warrior.").age(40).height("175cm").birthdate("Apr 16").anime(animes.get(8)).build(),
                        Character.builder().name("Vegeta").description("Proud Saiyan prince.").age(42).height("164cm").birthdate("Aug 14").anime(animes.get(8)).build(),
                        Character.builder().name("Ichigo Kurosaki").description("Teen Soul Reaper.").age(17).height("181cm").birthdate("Jul 15").anime(animes.get(9)).build(),
                        Character.builder().name("Rukia Kuchiki").description("Noble Soul Reaper.").age(150).height("144cm").birthdate("Unknown").anime(animes.get(9)).build()
                );
                characterRepository.saveAll(characters);
            }
        };
    }
}
