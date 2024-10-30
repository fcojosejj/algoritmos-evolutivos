import common.Config;

import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        try {
            String[] stringSeeds = Config.getSetting("global.seeds").split(",");
            long[] seeds = new long[stringSeeds.length];
            for (int i = 0; i < stringSeeds.length; i++) {
                seeds[i] = Long.parseLong(stringSeeds[i]);
            }
            String[] evaluationFunctions = new String[]{"ackley", "griewank", "rastringin", "schewefel", "perm0db", "rothyp", "rosenbrock", "michalewicz", "trid", "dixonprice", "MAPE", "RMSE"};

            while (true) {
                System.out.println("Selecciona qué opción deseas ejecutar. Se hará una ejecución múltiple para todos los algoritmos.");
                System.out.println("[1] Ejecutar EvM");
                System.out.println("[2] Ejecutar EvBLX");
                System.out.println("[3] Ejecutar DE");
                System.out.println("[0] Salir del programa");
                System.out.println("\nSelecciona una opción: ");
                Scanner scanner = new Scanner(System.in);
                int opcion = scanner.nextInt();
                switch (opcion) {
                    case 1: {
                        Multiexecutor multiexecutor = new Multiexecutor(seeds, evaluationFunctions, "AlgEvM_Clase03_Grupo04");
                        multiexecutor.execute();
                        break;
                    }
                    case 2: {
                        Multiexecutor multiexecutor = new Multiexecutor(seeds, evaluationFunctions, "AlgEvBLX_Clase03_Grupo04");
                        multiexecutor.execute();
                        break;
                    }
                    case 3: {
                        Multiexecutor multiexecutor = new Multiexecutor(seeds, evaluationFunctions, "AlgDE_Clase03_Grupo04");
                        multiexecutor.execute();
                        break;
                    }
                    case 0: {
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println("Opción no encontrada. Elige una opción del menú:");
                        break;
                    }
                }
            }

        } catch (Exception e) {
            Config.getLogger().log(Level.SEVERE, "There was an error!", e);
            System.exit(1);
        }
    }
}