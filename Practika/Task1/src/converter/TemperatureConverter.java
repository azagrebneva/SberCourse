package converter;

public class TemperatureConverter {

    public static double toFahrenheitDegrees(double celsius){
        return (9.0/5*celsius + 32);
    }

    public static double toKelvinDegrees(double celsius){
        return (celsius + 273.15);
    }

    public static double toReaumurDegrees(double celsius){
        return (celsius*0.8);
    }

    public static double toRankinDegrees(double celsius){
        return (celsius - 491.67)*5./9.;
    }

    public static double toRemerDegrees(double celsius){
        return (21./40.*celsius + 7.5);
    }

    public static double toDelilahDegrees(double celsius){
        return (100 - celsius)*3./2.;
    }

    public static double toHooksDegrees(double celsius){
        return celsius*12./5.;
    }

    public static double toDaltonDegrees(double celsius){
        return 100.*(Math.log(celsius + 273.15) - Math.log(273.15))/
                (Math.log(373.15) - Math.log(273.15));
    }

    public static double toPlanckDegrees(double celsius){
        return 1.416784*toKelvinDegrees(celsius)*1e32;
    }
}
