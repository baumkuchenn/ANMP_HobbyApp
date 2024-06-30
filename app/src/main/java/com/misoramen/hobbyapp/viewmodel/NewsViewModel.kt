package com.misoramen.hobbyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.misoramen.hobbyapp.model.Contents
import com.misoramen.hobbyapp.model.Genre
import com.misoramen.hobbyapp.model.News
import com.misoramen.hobbyapp.model.NewsWithAuthor
import com.misoramen.hobbyapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewsViewModel(app: Application): AndroidViewModel(app), CoroutineScope {
    val newsLD = MutableLiveData<ArrayList<NewsWithAuthor>>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    fun loadNews(){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            var result = db.hobbyDao().selectAllNews()
            if(result.isEmpty()){
                val genreList = listOf(
                    Genre("MotoGP", "Highest class of motorcycle road racing events held on road circuits sanctioned by the Fédération Internationale de Motocyclisme", "2024-04-17 03:00:00"),
                    Genre("Formula 1", "Highest class of international racing for open-wheel single-seater formula racing cars sanctioned by the Fédération Internationale de l'Automobile", "2024-04-17 03:00:00"),
                    Genre("World Endurance Championship", "Auto racing world championship organized by the Automobile Club de l'Ouest and sanctioned by the Fédération Internationale de l'Automobile", "2024-04-17 03:00:00"),
                    Genre("IndyCar", "highest class of American open-wheel car racing in the United States", "2024-04-17 03:00:00"))
                db.hobbyDao().insertGenres(genreList)

                val newsList = listOf(
                    News("SAINZ CONCEDES HIS F1 FUTURE COULD BE DEPENDENT ON OTHERS AS HE LOOKS TO \'KEEP PUSHING\' FOR NEXT DRIVE", "Carlos Sainz has fielded more questions over his future F1 plans, stating that he has \'no clue\' where he will be racing next year as things stand, while vowing to \'keep pushing\' to impress.", "https://media.formula1.com/image/upload/f_auto,c_limit,w_960,q_auto/t_16by9Centre/f_auto/q_auto/trackside-images/2024/F1_Grand_Prix_of_Japan___Previews/2137069299", "2024-04-17 03:06:07", 1, 2),
                    News("ALL-TIME CLASSIC: VIÑALES MAKES HISTORY WITH STUNNING COTA COMEBACK", "The Spaniard becomes the first rider to win a Grand Prix with three different manufacturers in a stunning race that will go down in the history books", "https://resources.motogp.pulselive.com/photo-resources/2024/04/14/a3093f9a-7faf-4255-b750-388f1ea9b0de/Report_MGP_RACE.jpg?width=2880&height=1620", "2024-04-17 05:02:53", 1, 1),
                    News("PEUGEOT TOTALENERGIES BREAKS COVER ON 2024 9X8 HYPERCAR", "Peugeot TotalEnergies has unveiled the upgrades brought to their Hypercar. The 2024 9X8 will make its competitive début at the 6 Hours of Imola, round two of the FIA WEC.", "https://www.fiawec.com/media/cache/news_list/assets/fileuploads/66/02/66028d41c2cf4.jpg", "2024-04-17 05:02:53", 1, 3),
                    News("PREMA BRINGS WINNING JUNIOR PEDIGREE TO NTT INDYCAR SERIES", "PREMA Racing may be not as well known in North America as in European racing circles, but the Italian team joining the NTT INDYCAR SERIES starting in 2025 with a two-car, Chevrolet-powered operation has boasted a veritable all-star team of junior drivers in its lineup for the last 40 years.", "https://www.indycar.com/-/media/IndyCar/News/Standard/2024/04/04-11-FRO-Prema.jpg?h=564&iar=0&w=940", "2024-04-17 05:05:38", 1, 4))
                db.hobbyDao().insertNews(newsList)

                val contentList = listOf(
                    Contents("This Monday, March 25, Peugeot TotalEnergies has lifted the veil off their brand-new 2024 9X8 Hypercar. After entering the FIA WEC\'s premier class in 2022, the French constructor decided last year to introduce radical aerodynamic upgrades for the upcoming 2024 season. The latest version of the Lion-emblazoned Hypercar now features a rear wing and different tyre widths. \"The idea was therefore to go back to a car design that is similar to our rival\'s car design\", details Olivier Jansonnie, Peugeot Sport Technical Director. \"Strictly speaking, it\'s not a new car, as it has the same chassis.\"", "2024-04-17 08:08:11", 3),
                    Contents("Before the FIA WEC\'s season launch, Peugeot TotalEnergies kept on operating their two 9X8 - entrusted to Nico Müller, Jean-Eric Vergne and Mikkel Jensen for the #93 and to Paul Di Resta, Loïc Duval et Stoffel Vandoorne in the #94 - while developing a new challenger. \"We were able to rely on the unwavering commitment of Team Peugeot TotalEnergies and we\'re now really looking forward to showing off the result of their hard work\", adds Jansonnie. \"The target is clearly to get back among the frontrunners, fight regularly for podiums and even challenge for race wins.\"", "2024-04-17 08:08:11", 3),
                    Contents("Peugeot Sport Technical Director teases that there’s more to the 2024 9X8 than meets the eye with the upgrades not confined to its aerodynamic balance. \"We decided to use this new homologation to add some reliability and performance upgrades to give us the best opportunities in the championship.\" According to Jean-Marc Finot, Stellantis Motorsport Senior VP, simulations and initial track testing sessions have shown that the car has more performance.", "2024-04-17 08:08:52", 3),
                    Contents("The 2024 9X8 also boasts a striking new livery recognizing Peugeot TotalEnergies’ team spirit and embodied by the brand’s feline emblem. “We came up with a design that deploys the ‘lion head’ in different scales”, outlines Matthias Hossann, Peugeot Brand Design Director. “This graphic design, symbolising a ‘pack of lions’, conveys the sense of collective that reflects the values of Endurance racing perfectly.”", "2024-04-17 08:08:52", 3),
                    Contents("After making its final appearance at the Qatar Airways Qatar 1812km, the opening round of the 2024 FIA WEC in which Peugeot TotalEnergies challenged for the win, the 2023 version of the 9X8 is now set to make way for the 2024 version at the 6 Hours of Imola, round two of the season, on April 21.", "2024-04-17 08:09:03", 3),
                    Contents("Sainz entered the 2024 season fresh from Ferrari’s decision to sign current Mercedes driver and seven-time world champion Lewis Hamilton for 2025, meaning it will be his fourth and final term with the Scuderia. Despite that disappointment, Sainz has been one of the stars of the campaign so far, kicking off with a podium in Bahrain and bouncing back from appendicitis – and subsequent surgery – to claim a stunning victory in Australia.", "2024-04-18 13:17:52", 1),
                    Contents("Following another podium finish in Japan, Sainz sits fourth in the drivers’ standings, even with the missed Saudi Arabia weekend, prompting plenty of paddock chatter about where he could be racing in the future. Several teams, including the likes of Red Bull and Mercedes, are yet to confirm their full 2025 line-ups, and Sainz, speaking before Fernando Alonso’s renewal with Aston Martin was announced, admitted he is talking to “many teams”.", "2024-04-18 13:17:52", 1),
                    Contents("Asked in an interview with Sky Sports F1 about the noise surrounding his future plans, and if he knows the next team he will be racing for, Sainz said: “Honestly, unfortunately I have no clue where I’m going to be next year. “It’s true that we’re talking to many teams. I just need to keep focused on what I’m doing, just prove to myself, prove to everyone that when I’m given a fast car, I’m maximising what I’m given and I deliver.", "2024-04-18 13:19:34", 1),
                    Contents("“It’s been a strong start to the season. It’s true that with this car you can shine a bit more; with last year’s car I’ve done performances similar to this year, but you couldn’t shine. “It was a much more difficult car to drive [last year], a lot more [tyre] deg, in the race you were looking backwards, this year we’re looking forwards, you’re overtaking and this allows you to shine a bit more. It helps, but I’ll keep pushing.”", "2024-04-18 13:19:34", 1),
                    Contents("Sainz was then pushed on whether he feels he is in control of his future, or if it will be dependent on other people’s decisions first, and the Spaniard responded that it is a bit of both. “I think it also depends on other people’s… on other teams, what they choose to go for,” he commented. “They know I’m available, so let’s see what happens!”", "2024-04-18 13:19:49", 1),
                    Contents("If you’re going to make history, it’s best to do it in style – and Maverick Viñales (Aprilia Racing) got the memo. After a chaotic start that saw the #12 punted down to P11, the race was absolutely on to claw back through the field and unleash his stunning pace, so that he did. Pass after pass after pass put him back in the postcode of the podium, and from there he threaded the needle to the front and got the hammer down to make history as the first to win with three manufacturers in the MotoGP™ era. And did we mention it was one of the greatest races of all time?\r\n", "2024-04-18 13:25:19", 2),
                    Contents("It was a race full of drama at the Circuit Of The Americas, with the most exciting sport in the world riding the 200mph rodeo, there were thrilling battles for the lead and Champions crashing out in a breathtaking Grand Prix that will go down in the history books for more than one reason.", "2024-04-18 13:25:19", 2),
                    Contents("Behind Batmav, and not by much, Pedro Acosta (Red Bull GASGAS Tech3) came home second to become the youngest rider to take back-to-back MotoGP™ podiums, and the rookie was box office – as ever. Enea Bastianini (Ducati Lenovo Team) completed the podium, making a late move on Jorge Martin (Prima Pramac Racing). So where was Marc Marquez (Gresini Racing MotoGP™)? The #93 crashed out after he’d just grabbed the lead back from Acosta, sliding out over the run off and forced to watch from the sidelines.", "2024-04-18 13:25:53", 2),
                    Contents("22 of the world\'s best riders stormed to turn one, fighting for the same piece of tarmac, with Acosta leading after a historic launch off the line. However, there was drama at turn one for both Aprilia Racing machines with Aleix Espargaro and polesitter and Sprint winner Viñales having a disastrous start after a move from Martin up the inside caused a shuffle. The race was on...", "2024-04-18 13:25:53", 2),
                    Contents("Martin\'s aggressive moves in the opening stage of the race saw him able to storm through to second at the end of the first lap. Martin then responded to leader Acosta, testing the rookie, but the test was passed. Meanwhile, behind it was a nail-biting start for Marc Marquez, who was inside the top five and touched with Jack Miller (Red Bull KTM Factory Racing) on the exit of turn 11, both continuing unabated.", "2024-04-18 13:26:20", 2),
                    Contents("The action continued, causing the crowd to roar as Martin finally found a way through on Acosta. The rookie then dropped to fourth, but setting his sights on reigning World Champion Francesco Bagnaia (Ducati Lenovo Team), who just lost a place to Marc Marquez.", "2024-04-18 13:26:20", 2),
                    Contents("All eyes were soon on the eight-time World Champion, who attempted to find a gap in Martin’s armour to steal the lead at the final corner, making contact with the 2023 runner-up. This jaw-dropping near miss dropped Marc Marquez down to fourth – forcing the Spaniard to go back into battle with Bagnaia.", "2024-04-18 13:26:42", 2),
                    Contents("Martin began to put the hammer down early and build an early lead of nine-tenths from Marquez and Acosta, who continued in second and third. However, the lead was short-lived as Marc Marquez made time on the Prima Pramac Racing rider and soon stole first place from Martin at turn 11, pushing the #89 wide in a show-stopping move. But just one lap later it all ended in disaster, at the very same corner, as Marc Marquez lost the front - crashing out of the Grand Prix, to the disappointment of the American fans.", "2024-04-18 13:26:42", 2),
                    Contents("The next rider to charge to the front was Viñales, showing resilience after dropping back to ninth at the end of the first lap. ‘BatMav’ carved his way through the field, demonstrating his determination to return to the front on the road to make history.  Viñales eventually battled to fifth before finding a way through on Martin before going head-to-head with Acosta in an unbelievable duel for the victory. The #12 made a move stick with eight laps remaining, and the rest was history – stretching a one-second lead.", "2024-04-18 13:28:54", 2),
                    Contents("Acosta gave it everything, but it would not be enough as Viñales crossed the chequered flag, which was waved by Sonic the Hedgehog at the Americas GP. Acosta was a mere 1.728 behind – becoming the youngest rider to take two consecutive podiums. Joining them on the podium was the ‘Beast’ with Bastianini getting the better of Martin, who missed out on a podium finish by less than two seconds. Bagnaia rounded out the top five places – losing time late in the race but scoring solid points in Austin.\r\n", "2024-04-18 13:28:54", 2),
                    Contents("Fabio Di Giannantonio (Pertamina Enduro VR46 Racing Team) came across the line to take sixth position, finishing ahead of the second Aprilia Racing machine of Aleix Espargaro. Marco Bezzecchi took eighth place with both Pertamina Enduro VR46 Racing Team Ducati machines scoring points. Brad Binder (Red Bull KTM Factory Racing) ended a great recovery ride in ninth after qualifying in 17th. The South African finished the Grand Prix ahead of Raul Fernandez, who rounded out the top 10 at Trackhouse Racing’s home round.", "2024-04-18 13:29:18", 2),
                    Contents("The MotoGP™ paddock now heads back to Europe at the historic Circuit de Jerez – Angel Nieto, a battleground where many races have been decided at the final corner, including Valentino Rossi and Sete Gibernau in 2005 and Marc Marquez and Jorge Lorenzo in 2013. So, make sure you don’t miss the next chapter, and keep up to date with all the action on motogp.com.", "2024-04-18 13:29:18", 2),
                    Contents("Fabio Di Giannantonio (Pertamina Enduro VR46 Racing Team) came across the line to take sixth position, finishing ahead of the second Aprilia Racing machine of Aleix Espargaro. Marco Bezzecchi took eighth place with both Pertamina Enduro VR46 Racing Team Ducati machines scoring points. Brad Binder (Red Bull KTM Factory Racing) ended a great recovery ride in ninth after qualifying in 17th. The South African finished the Grand Prix ahead of Raul Fernandez, who rounded out the top 10 at Trackhouse Racing’s home round.", "2024-04-18 13:30:11", 2),
                    Contents("The MotoGP™ paddock now heads back to Europe at the historic Circuit de Jerez – Angel Nieto, a battleground where many races have been decided at the final corner, including Valentino Rossi and Sete Gibernau in 2005 and Marc Marquez and Jorge Lorenzo in 2013. So, make sure you don’t miss the next chapter, and keep up to date with all the action on motogp.com.", "2024-04-18 13:30:11", 2),
                    Contents("The team started in 1983 in the Italian Formula 3 championship and has evolved to where this season it is competing in FIA Formula 2 and FIA Formula 3, along with Formula Regional European Championship, Formula 4 and the all-women driver category F1 Academy. A look at the names and faces in the PREMA Hall of Fame is impressive.", "2024-04-18 13:33:33", 4),
                    Contents("PREMA has produced 21 Formula One drivers since the team was formed in 1983. Eight of the 20 full-time drivers on the 2024 F1 grid drove for PREMA: Charles Leclerc of Ferrari, Oscar Piastri of McLaren, Pierre Gasly and Esteban Ocon of Alpine, Lance Stroll of Aston Martin, Logan Sargeant of Williams, and Valtteri Bottas and Zhou Guanyu of Kick Sauber. Oliver Bearman, who filled in for Ferrari in Saudi Arabia after Carlos Sainz underwent an emergency appendectomy, drives for PREMA this season in the FIA Formula 2 Championship.", "2024-04-18 13:33:33", 4),
                    Contents("The team also has graduated 14 drivers to the INDYCAR SERIES, including active series drivers Marcus Armstrong of Chip Ganassi Racing, Felix Rosenqvist of Meyer Shank Racing and Callum Ilott of Arrow McLaren. INDYCAR SERIES race winners Ryan Briscoe and Charlie Kimball also drove for PREMA during their junior careers, and 1995 Indianapolis 500 winner and INDYCAR SERIES champion Jacques Villeneuve raced for the team on his climb up the single-seater ladder.\r\n", "2024-04-18 13:33:55", 4),
                    Contents("All that star power has produced a staggering 79 championships in various formulas in 41 seasons of competition. PREMA may not be immediately recognizable yet in North America, but the team is considered one of the three forces of Italian motor racing, sitting just behind F1 teams Scuderia Ferrari and Visa CashApp RB. Rosenqvist enjoyed a banner campaign in 2015 with PREMA. He dominated the European Formula 3 Championship, romping to the title with 13 victories, 24 podium finishes and 17 poles. He also won the prestigious Macau Grand Prix from the pole for the second straight year.", "2024-04-18 13:33:55", 4),
                    Contents("Armstrong won the Italian F4 championship with PREMA in 2017 and finished second in the FIA Formula 3 Championship in 2019 with the team. Ilott won six races and 10 poles en route to finishing fourth in the FIA Formula 3 European Championship with the team in 2017. Villeneuve launched his single-seater career with PREMA for three seasons from 1989-91, driving in the Italian Formula 3 championship during the team’s early years before he shifted his focus to racing in Japan and North America.\r\n", "2024-04-18 13:34:19", 4),
                    Contents("Some of the drivers in PREMA’s past galaxy of stars, including the current NTT INDYCAR SERIES drivers, needled PREMA Team Principal Rene Rosin in recent years about expanding the team’s horizons into North America’s premier open-wheel racing series. “We just celebrated our 40th anniversary in December,” Rosin said. “Most of them (past drivers) have been able to join us in Venice in our event. We’re always very close to them and have a very friendly relationship with all of them. We always talk together.\r\n", "2024-04-18 13:34:19", 4),
                    Contents("“They always say, Why not coming to America? Why not do INDYCAR? It would be something very good for you, very good for the team. That is something we are looking forward in 2025 now.”", "2024-04-18 13:34:31", 4))
                db.hobbyDao().insertContents(contentList)
                result = db.hobbyDao().selectAllNews()
            }
            newsLD.postValue(result as ArrayList<NewsWithAuthor>)
        }
    }

    fun getCertainNews(idNews: Int){
        newsLoadErrorLD.value = false
        loadingLD.value = false

        launch {
            val db = buildDb(getApplication())
            val result = db.hobbyDao().selectCertainNews(idNews)
            newsLD.postValue(result as ArrayList<NewsWithAuthor>)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}