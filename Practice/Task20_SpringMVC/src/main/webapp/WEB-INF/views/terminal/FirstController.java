//package spring.course.controllers;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//@RequestMapping("/first")
//public class FirstController {
//
//    // http://localhost:8080/hello?name=anna&surname=Zagrebneva
//    @GetMapping("/hello")
//    public String helloPage(HttpServletRequest request,
//                            Model model){
//        String name = request.getParameter("name");
//        String surname = request.getParameter("surname");
//
//        model.addAttribute("message",
//                "Hello, " + name +" "+surname + "!");
//        return "first/hello";
//    }
//
//    @GetMapping("/goodbye")
//    public String goodByePage(@RequestParam(value = "name", required = false) String name,
//                              @RequestParam(value = "surname", required = false) String surname){
//    //public String goodByePage(){
//        System.out.println("Goodbye " + name + " " + surname);
//        return "first/goodbye";
//    }
//
//    // http://localhost:8080/first/calculator?a=1&b=2&action=addition
//    @RequestMapping("/calculator")
//    public String calculator(@RequestParam(value = "a") int a,
//                             @RequestParam(value = "b") int b,
//                             @RequestParam(value = "action") String action,
//                             Model model) {
//        double result;
//        switch (action) {
//            case "multiplication":
//                result = a * b;
//                break;
//            case "division":
//                result = a / (double) b;
//                break;
//            case "subtraction":
//                result = a - b;
//                break;
//            case "addition":
//                result = a + b;
//                break;
//            default: result = 0;
//        }
//
//        model.addAttribute("result", result);
//        return "first/calculator";
//    }
//
//}
