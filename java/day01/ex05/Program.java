public class Program {
    public static void main(String[] args) {
        boolean devMode = false;
        if (args.length == 1 && args[0].equals("--profile=dev")) {
            devMode = true;
        } else if (args.length != 0
                && !(args.length == 1 && args[0].equals("--profile=production"))) {
            System.err.println("Usage: java Program [--profile=dev|--profile=production]");
            return;
        }

        Menu menu = new Menu(devMode);
        menu.run();
    }
}
