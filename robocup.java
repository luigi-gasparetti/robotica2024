import lejos.nxt.*;
import lejos.util.Delay;
import lejos.robotics.navigation.*;
import lejos.robotics.mapping.*;
import lejos.robotics.pathfinding.*;
import lejos.robotics.localization.*;

import java.util.ArrayList;

import lejos.geom.*;

  public class robocup {

    public static void main(String[] args) {

        class destino{
        
          Waypoint porta1;
          Waypoint porta2;
          Waypoint dentro;
          Line parede1;
          Line parede2;

          public destino(Waypoint w1, Waypoint w2, Waypoint w3, Line p1, Line p2){
            this.porta1 = w1;
            this.porta2 = w2;
            this.dentro = w3;
            this.parede1 = p1;
            this.parede2 = p2;
          }
          
          public Waypoint getW1(){
              return this.porta1;
          }
          public Waypoint getW2(){
              return this.porta2;
          }

          public Waypoint getW3(){
              return this.dentro;
          }


          public float distanciaPontos(Waypoint w1, Waypoint w2){
            return (float) Math.sqrt(Math.pow(w1.x - w2.x, 2) + Math.pow(w1.y - w2.y, 2));
          }

          public float distanciaPath(ArrayList<Waypoint> path){
            float distancia = 0;

            for(int i = 0; i < path.size() - 1; i++){
              distancia += distanciaPontos(path.get(i),path.get(i+1));
            }
            return distancia;
          }
        }

      try {

        System.out.println("Running");  

        int B1 = Button.waitForAnyPress();
        int B2 = Button.waitForAnyPress();
        System.out.println("Botao 1 " + B1);
        System.out.println("Botao 2 " + B2); 
        
        Delay.msDelay(500);  

        // Inicializa o DifferentialPilot para controlar os motores do robô
        DifferentialPilot pilot = new DifferentialPilot(2.205 * 2.56, 4.527 * 2.56, Motor.A, Motor.B, false);
        // Define a velocidade de deslocamento linear para 15 cm/s
        pilot.setTravelSpeed(15);  
        // Define a velocidade de rotação para 30 graus/s
        pilot.setRotateSpeed(30);  

        // Provedor de Pose baseado em Odometria que calcula a posição e orientação do robô
        OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);

        // Cria um objeto Navigator para controlar a navegação do robô
        Navigator navigator = new Navigator(pilot);

        // Define um conjunto de linhas que representarão obstáculos no mapa
        Line[] lines = {
          //BORDAS DO MAPA
          new Line(-10, -10, -10, 160),
          new Line(-10, 160, 160, 160),
          new Line(160, 160, 160, -10),
          new Line(160, -10, -10, -10),
          //SCHOOL
          new Line(0, 30, 30, 30),
          new Line(30, 30, 30, 60),
          new Line(30, 60, 0, 60),
          //BAKERY
          new Line(0, 90, 30, 90),
          new Line(30, 90, 30, 120),
          new Line(30, 120, 0, 120),
          //MUSEUM
          new Line(150, 90, 120, 90),
          new Line(120, 90, 120, 120),
          new Line(120, 120, 150, 120),
          //LIBRARY
          new Line(150, 30, 120, 30),
          new Line(120, 30, 120, 60),
          new Line(120, 60, 150, 60),
          //CITY HALL
          new Line(60, 30, 90, 30),
          new Line(90, 30, 90, 60),
          new Line(90, 60, 60, 60),
          new Line(60, 60, 60, 30),
          //DRUGSTORE
          new Line(60, 90, 90, 90),
          new Line(90, 90, 90, 120),
          new Line(90, 120, 60, 120),
          new Line(60, 120, 60, 90)
        };


        Waypoint wp1 = new Waypoint(0, 0);
        Waypoint wp2 = new Waypoint(0, 0);
        Waypoint wp3 = new Waypoint(0, 0);
        Line parede1 = new Line(0,0,0,0);
        Line parede2 = new Line(0,0,0,0);
        Waypoint inicio = new Waypoint (0,0);
        Waypoint menor_caminho = new Waypoint(0,0);

        //SCHOOL
        if(B1 == 1 &&  B2 == 1){
          wp1 = new Waypoint(30, 45);
          wp2 = new Waypoint(15, 60);
          wp3 = new Waypoint(15, 45);
          parede1 = new Line(42, 18, 42, 72);
          parede2 = new Line(42, 72, 0, 72);
        }
        //BAKERY
        if(B1 == 1 &&  B2 == 2){
          wp1 = new Waypoint(30, 105);
          wp2 = new Waypoint(15, 120);
          wp3 = new Waypoint(15, 105);
          parede1 = new Line(42, 78, 42, 132);
          parede2 = new Line(42, 132, 0, 132);
        }
        //MUSEUM
        if(B1 == 1 &&  B2 == 4){
          wp1 = new Waypoint(135, 90);
          wp2 = new Waypoint(120, 105);
          wp3 = new Waypoint(135, 105);
          parede1 = new Line(150, 78, 108, 78);
          parede2 = new Line(108, 78, 108, 132);
        }
        //LIBRARY
        if(B1 == 1 &&  B2 == 8){
          wp1 = new Waypoint(135, 30);
          wp2 = new Waypoint(120, 45);
          wp3 = new Waypoint(135, 45);
          parede1 = new Line(150, 18, 108, 18);
          parede2 = new Line(108, 18, 108, 72);
        }
        //CITY HALL
        if(B1 == 2 &&  B2 == 1){
          wp1 = new Waypoint(60, 45);
          wp2 = new Waypoint(90, 45);
          wp3 = new Waypoint(75, 45);
          parede1 = new Line(48, 72, 48, 18);
          parede2 = new Line(102, 18, 102, 72);
        }
        //DRUGSTORE
        if(B1 == 2 &&  B2 == 4){
          wp1 = new Waypoint(75, 90);
          wp2 = new Waypoint(75, 120);
          wp3 = new Waypoint(75, 105);
          parede1 = new Line(48, 78, 102, 78);
          parede2 = new Line(102, 132, 48, 132);
        }
        //PARK
        if(B1 == 2 &&  B2 == 8){
          wp1 = new Waypoint(15, 150);
          wp2 = new Waypoint(75, 150);
        }

        destino lugar = new destino(wp1,wp2,wp3,parede1,parede2);

        // Cria um mapa de linhas (LineMap) que inclui os obstáculos e os limites do mapa
        //Rectangle bounds = new Rectangle(160, -10, 170, 170);
        Rectangle bounds = new Rectangle(0, 0, 200, 200);
        LineMap map = new LineMap(lines, bounds);


        ShortestPathFinder pathFinder = new ShortestPathFinder(map);
        
        pathFinder.lengthenLines(12);
        
        // CALCULA A MENOR ROTA
        Path path1 = pathFinder.findRoute(poseProvider.getPose(), lugar.getW1());
            
        Path path2 = pathFinder.findRoute(poseProvider.getPose(), lugar.getW2());

        float length1 = lugar.distanciaPath((ArrayList<Waypoint>)path1);
        float length2 = lugar.distanciaPath((ArrayList<Waypoint>)path2);

        if(length1 < length2){
          menor_caminho = lugar.getW1();
        }
        else{
          menor_caminho = lugar.getW2();
        }
        
        //Waypoint menor_caminho = lugar.get_menor(pathFinder, poseProvider.getPose());
        navigator.setPath(pathFinder.findRoute(poseProvider.getPose(), menor_caminho));
        navigator.followPath();

        System.out.println("DESTINO " + menor_caminho);
        Button.waitForAnyPress();

        //ESPERA O ROBO COMPLETAR O CAMINHO
        while (!navigator.pathCompleted()){}

        //ADICIONAR AQ PARA ENTRAR NO WAYPOINT DE DENTRO
        //navigator.setPath(pathFinder.findRoute(poseProvider.getPose(), ));
        //navigator.followPath();
        //while (!navigator.pathCompleted()){}

        //VOLTA DO ROBO
        navigator.setPath(pathFinder.findRoute(poseProvider.getPose(), inicio));
        navigator.followPath();
      
        Button.waitForAnyPress();

      } catch (Exception e) {
        // Imprime qualquer exceção que ocorra durante a execução
        System.out.println(e);
        Button.waitForAnyPress();
      }
    }
  }

  /*
  Line[] lines = {
            //BORDAS DO MAPA
            new Line(0, 0, 0, 150),
            new Line(0, 150, 150, 150),
            new Line(150, 150, 0, 150),
            new Line(0, 150, 0, 0),
            //SCHOOL
            new Line(0, 30, 30, 30),
            new Line(30, 30, 30, 60),
            new Line(30, 60, 0, 60),
            //BAKERY
            new Line(0, 90, 30, 90),
            new Line(30, 90, 30, 120),
            new Line(30, 120, 0, 120),
            //MUSEUM
            new Line(150, 120, 120, 120),
            new Line(120, 120, 120, 150),
            new Line(120, 150, 150, 150),
            //LIBRARY
            new Line(150, 30, 120, 30),
            new Line(120, 30, 120, 60),
            new Line(120, 60, 150, 60),
            //CITY HALL
            new Line(60, 30, 90, 30),
            new Line(90, 30, 90, 60),
            new Line(90, 60, 60, 60),
            new Line(60, 60, 60, 30),
            //DRUGSTORE
            new Line(60, 120, 90, 120),
            new Line(90, 120, 90, 150),
            new Line(90, 150, 60, 150),
            new Line(60, 150, 60, 120)
        };

        public float distanciaPath(Path path){
            float distancia = 0;
            List<Waypoint> waypoints = path.getWaypoints();  // Não precisa de casting
            for(int i = 0; i < waypoints.size() - 1; i++){
                distancia += distanciaPontos(waypoints.get(i), waypoints.get(i+1));
            }
            return distancia;
        }

        Line[] lines = {
            //BORDAS DO MAPA
            new Line(0, 0, 0, 150),
            new Line(0, 150, 150, 150),
            new Line(150, 150, 150, 0),
            new Line(150, 0, 0, 0),
            //SCHOOL
            new Line(0, 18, 42, 18),
            new Line(42, 18, 42, 72),
            new Line(42, 72, 0, 72),
            //BAKERY
            new Line(0, 78, 42, 78),
            new Line(42, 78, 42, 132),
            new Line(42, 132, 0, 132),
            //MUSEUM
            new Line(150, 78, 108, 78),
            new Line(108, 78, 108, 132),
            new Line(108, 132, 150, 132),
            //LIBRARY
            new Line(150, 18, 108, 18),
            new Line(108, 18, 108, 72),
            new Line(108, 72, 150, 72),
            //CITY HALL
            new Line(48, 18, 102, 18),
            new Line(102, 18, 102, 72),
            new Line(102, 72, 48, 72),
            new Line(48, 72, 48, 18),
            //DRUGSTORE
            new Line(48, 78, 102, 78),
            new Line(102, 78, 102, 132),
            new Line(102, 132, 48, 132),
            new Line(48, 132, 48, 78)
        };
  */
