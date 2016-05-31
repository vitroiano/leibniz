package middleware;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by davi_000 on 30/05/2016.
 */
public class LeibnizWorker implements Runnable {
    private long tempoInicio;
    private Integer iterations;
    private Integer threads;
    private int serverId;
    private double[] result;
    private InetAddress server;

    public LeibnizWorker(long tempoInicio, Integer iterations, int serverId, double[] result, LeibnizServer server) {
        this.tempoInicio = tempoInicio;
        this.iterations = iterations;
        this.serverId = serverId;
        this.result = result;
        this.server = server;
    }

    @Override
    public void run() {
        middleware.InterfaceLeibniz leibniz;

        try {
            String URI = "rmi://" + InetAddress.getLocalHost().getHostAddress() + ":2220/LeibnizMiddleware";
            leibniz = (InterfaceLeibniz) Naming.lookup(URI);

            System.out.println("Tempo Total: " + (System.currentTimeMillis() - tempoInicio));

            result[serverId] = leibniz.calc(iterations, threads);
        } catch (NotBoundException e) {
            System.out.println("Ocorreu um erro com a porta. Verifique seu firewall.");
        } catch (MalformedURLException e) {
            System.out.println("Ocorreu um erro ao conectar ao servidor. Verifique a URL.");
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro no servidor.");
        } catch (UnknownHostException e) {
            System.out.println("Erro ao conectar ao servidor " + server.getAddress());
        }
    }
}
