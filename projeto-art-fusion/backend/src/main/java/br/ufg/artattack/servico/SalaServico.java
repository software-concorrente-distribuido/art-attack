package br.ufg.artattack.servico;

import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.modelo.Sala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SalaServico {

        private static Map<String, Sala> salas = new HashMap<>();

        @Autowired
        ArteServico arteServico;

        public Sala criarSala(Long idArte) throws Exception {

            String uuid = UUID.randomUUID().toString();

            Arte arte =  arteServico.arteRepositorio.findById(idArte).orElseThrow( () -> new Exception("Arte não encontrada"));

            Sala sala = new Sala(uuid,arte);

            salas.put(uuid, sala);
            return sala;
        }

        public Sala obterSala(String id) {
            var sala =  salas.get(id);
            if(sala==null){
                throw new IllegalArgumentException("Sala já fechada");
            }
            return sala;
        }

        public Sala fecharSala(String uuid){
            Sala sala = salas.remove(uuid);

            if(sala==null)
                throw new IllegalArgumentException("Não foi possível remover a sala, pois ela não está aberta!");

            return sala;
        }

    public Sala obterSalaPorIdArte(String arteId) throws Exception {

        for (Map.Entry<String, Sala> entry : salas.entrySet()) {

            Sala s = entry.getValue();

            if (Objects.equals(s.arte.id, Long.valueOf(arteId))) {
                return  s;
            }

        }

        throw new Exception("Não foi possível encontrar a susa Arte pelo ID fornecido");

    }

    // Classe Sala pode ser uma classe interna ou uma classe separada


}
