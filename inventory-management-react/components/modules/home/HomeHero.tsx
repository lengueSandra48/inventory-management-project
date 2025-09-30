import Link from "next/link"
import { ArrowRight } from "lucide-react"
import { Button } from "@/components/ui/button"

export function HomeHero() {
    return (
        <section className="py-20 px-4 sm:px-6 lg:px-8">
            <div className="max-w-7xl mx-auto text-center animate-fade-in">
                <h1 className="text-5xl md:text-6xl font-bold text-foreground mb-6">
                    Bienvenue sur <span className="text-primary">StockFlow</span>
                </h1>
                <p className="text-xl text-muted-foreground mb-8 max-w-3xl mx-auto">
                    La solution complète de gestion d'inventaire pour votre entreprise. Optimisez vos stocks, gérez vos commandes et suivez vos ventes en temps réel.
                </p>
                <div className="flex flex-col sm:flex-row gap-4 justify-center">
                    <Link href="/dashboard">
                        <Button size="lg" className="btn-animate">
                            Commencer avec StockFlow
                            <ArrowRight className="ml-2 h-5 w-5" />
                        </Button>
                    </Link>
                    <Link href="/login">
                        <Button size="lg" variant="outline" className="btn-animate">
                            {"J'ai déjà un compte"}
                        </Button>
                    </Link>
                </div>
            </div>
        </section>
    )
}