package edu.pucp.gtics.lab5_gtics_20211.controller;

import edu.pucp.gtics.lab5_gtics_20211.entity.Juegos;
import edu.pucp.gtics.lab5_gtics_20211.entity.JuegosUserDto;
import edu.pucp.gtics.lab5_gtics_20211.entity.Plataformas;
import edu.pucp.gtics.lab5_gtics_20211.entity.User;
import edu.pucp.gtics.lab5_gtics_20211.repository.JuegosRepository;
import edu.pucp.gtics.lab5_gtics_20211.repository.PlataformasRepository;
import edu.pucp.gtics.lab5_gtics_20211.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller

public class JuegosController {
    //

    @Autowired
    JuegosRepository juegosRepository;

    @Autowired
    PlataformasRepository plataformasRepository;

    @GetMapping(value = {"/juegos/lista", "/juegos"})
    public String listaJuegos (Model model, HttpSession session){

        User usuarioactual = (User) session.getAttribute("usuario");
        System.out.println(usuarioactual.getAutorizacion());
        System.out.println("#############################3");
        System.out.println(usuarioactual.getIdusuario());
        System.out.println(usuarioactual.getNombres());
        if(usuarioactual.getAutorizacion().equalsIgnoreCase("USER")){
            int idusuario = usuarioactual.getIdusuario();
            model.addAttribute("listajuegosxuser",juegosRepository.obtenerJuegosPorUser(idusuario));
            List<JuegosUserDto> b = juegosRepository.obtenerJuegosPorUser(idusuario);
            System.out.println("#################################3222222");

            return "juegos/comprado";

        }else if(usuarioactual.getAutorizacion().equalsIgnoreCase("ADMIN")){
            model.addAttribute("listajuegosprecioasc",juegosRepository.listaJuegosPrecioAsc());
            List<Juegos> a = juegosRepository.listaJuegosPrecioAsc();
            System.out.println("##############################3333");

            return "juegos/lista";
        }else{
            return "redirect:/vista";
        }
    }

    @GetMapping(value = {"", "/", "/vista"})
    public String vistaJuegos (Model model){
        model.addAttribute("listajuegosnombredesc",juegosRepository.listaJuegosNombreDesc());
        return "juegos/vista";
    }

    @GetMapping("/juegos/nuevo")
    public String nuevoJuegos(Model model, @ModelAttribute("juego") Juegos juego){
        model.addAttribute("listaPlata",plataformasRepository.findAll());
        //model.addAttribute("juego",juego);
        return "juegos/editarFrm.html";
    }

    @GetMapping("/juegos/editar")
    public String editarJuegos(@RequestParam("id") int id, Model model){
        Optional<Juegos> optional = juegosRepository.findById(id);
        if(optional.isPresent()){
            Juegos juegos = optional.get();
            model.addAttribute("juego",juegos);
            List<Plataformas> listaPlata = plataformasRepository.findAll();
            model.addAttribute("listaPlata",listaPlata);
            return "juegos/editarFrm.html";
        }
        return "juegos/lista";
    }

    @PostMapping("/juegos/guardar")
    public String guardarJuegos(Model model, RedirectAttributes attr, @ModelAttribute("juego") @Valid Juegos juego, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            model.addAttribute("listaPlata",plataformasRepository.findAll());
            return"juegos/editarFrm.html";
        }else{
            juegosRepository.save(juego);
            return "redirect:/juegos/lista";
        }

    }

    @GetMapping("/juegos/borrar")
    public String borrarDistribuidora(@RequestParam("id") int id){
        Optional<Juegos> opt = juegosRepository.findById(id);
        if (opt.isPresent()) {
            juegosRepository.deleteById(id);
        }
        return "redirect:/juegos/lista";
    }

}
